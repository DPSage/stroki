package ru.example.test.project.storki.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.example.test.project.storki.model.user.RoleRs;
import ru.example.test.project.storki.model.user.UserRs;
import ru.example.test.project.storki.service.api.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private final UserService userService;

    public AuthorizationFilter(UserService userService) {
        this.userService = userService;
    }

    private final Algorithm algorithm = Algorithm.HMAC256("secret-key".getBytes());
    private final JWTVerifier verifier = JWT.require(algorithm).build();

    private final String BEARER = "Bearer ";
    private final String EXPIRED_TOKEN = "The Token has expired";
    private final String AUTHORIZATION_REFRESH = AUTHORIZATION + "-Refresh";

    private final List<String> skipFilterUrl = new ArrayList<>() {{
        add("/api/user/registration");
        add("/v3/api-docs");
        add("/swagger-ui");
        add("/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config");
    }};

    /**
     * Метод проверяет авторизацию. <br/>
     * Если время <strong>"Authorization"</strong> закончилось, проверяется <strong>"Authorization-Refresh"</strong> и токены обновляются <br/>
     * Если время закончилось у <strong>"Authorization"</strong> и <strong>"Authorization-Refresh"</strong>, необходимо заново авторизовываться <br/>
     * <br/>
     * <strong>skipFilterUrl</strong> - список URL, для которых не нужно проверять аворизацию <br/>
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (skipFilterUrl.contains(request.getServletPath()) || request.getServletPath().matches("^/\\w+$")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
                try {
                    DecodedJWT decodedJWT = checkToken(authorizationHeader);
                    authorization(decodedJWT);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    if (e.getMessage().startsWith(EXPIRED_TOKEN)) {
                        refreshToken(request, response);
                        filterChain.doFilter(request, response);
                    } else {
                        errorLogging(response, e);
                    }
                }
            }
        }

    }

    private void authorization(DecodedJWT decodedJWT) {
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_REFRESH);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            try {
                DecodedJWT decodedJWT = checkToken(authorizationHeader);

                String username = decodedJWT.getSubject();
                UserRs user = userService.getUser(username);

                Map<String, String> newTokens = createNewTokens(request, response, user);
                decodedJWT = verifier.verify(newTokens.get("access-token"));
                authorization(decodedJWT);

                response.setHeader(AUTHORIZATION, newTokens.get("access-token"));
                response.setHeader(AUTHORIZATION_REFRESH, newTokens.get("refresh-token"));
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), newTokens);
            } catch (Exception e) {
                errorLogging(response, e);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }

    }

    private Map<String, String> createNewTokens(HttpServletRequest request, HttpServletResponse response, UserRs user) {
        Map<String, String> tokens = new HashMap<>();

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getRoles().stream().map(RoleRs::getName).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        tokens.put("access-token", accessToken);
        tokens.put("refresh-token", refreshToken);
        return tokens;
    }

    private void errorLogging(HttpServletResponse response, Exception e) throws IOException {
        log.error("Error logging in : {}", e.getMessage());

        response.setHeader("error", e.getMessage());
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        Map<String, String> error = new HashMap<>();
        error.put("error_message", e.getMessage());

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    private DecodedJWT checkToken(String authorizationHeader) {
        String token = authorizationHeader.substring(BEARER.length());
        return verifier.verify(token);
    }
}
