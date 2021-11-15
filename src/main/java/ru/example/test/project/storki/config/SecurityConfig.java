package ru.example.test.project.storki.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.example.test.project.storki.filter.AuthenticationFilter;
import ru.example.test.project.storki.filter.AuthorizationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AuthorizationFilter authorizationFilter;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        settingAuthorizeRequests(http);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(authenticationFilter());
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private AuthenticationFilter authenticationFilter() throws Exception {
//        authenticationFilter.setFilterProcessesUrl("/api/login");
        return new AuthenticationFilter(authenticationManagerBean());
    }

    private void settingAuthorizeRequests(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login/**", "/**", "/api/user/registration/**")
                .permitAll();

        http.authorizeRequests()
                .antMatchers("/api/links/**", "/api/link/**", "/api/user/info/**")
                .hasAnyAuthority("ROLE_USER");

        http.authorizeRequests()
                .antMatchers("/api/roles/**", "/api/role/**", "/api/users/**", "/api/user/**")
                .hasAnyAuthority("ROLE_ADMIN");

        http.authorizeRequests().anyRequest().authenticated();
    }


}
