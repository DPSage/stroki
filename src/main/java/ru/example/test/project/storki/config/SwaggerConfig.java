package ru.example.test.project.storki.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-UI сломался...
 *
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Test Project")
                .description("")
                .version("0.0.1-SNAPSHOT")
                .contact(apiContact());

    }

    private Contact apiContact() {
        return new Contact()
                .email("test@mail.ru")
                .name("Matthew")
                .url("https://github.com/matthew95011");
    }

}
