package com.study.bookstore.global.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @io.swagger.v3.oas.annotations.info.Info(
        title = "BASIC BOARD API",
        version = "v1"
    ),
    servers = {
        @io.swagger.v3.oas.annotations.servers.Server(
            url = "http://localhost:9090/",
            description = "local test"
        )
    }
)
public class SwaggerConfig {

}
