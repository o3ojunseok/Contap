package com.project.contap.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfig {
    //  swagger 접속 링크
    //  http://localhost:8080/swagger-ui.html

    @Bean
    public OpenAPI springContapOpenAPI() {
        Info info = new Info()
                .title("Contap")
                .version("V1.0")
                .description("")
                .contact(new Contact()
                        .name("Web Site")
                        .url("http://localhost:8080/"))
                .license(new License()
                        .name("Apache License Version 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0"));

        SecurityScheme auth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).in(SecurityScheme.In.HEADER).scheme("bearer").bearerFormat("");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("authorization");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("authorization",auth))
                .addSecurityItem(securityRequirement)
                .info(info);
    }


}

