package Mua.Mua_backend.global.config;

import Mua.Mua_backend.global.swagger.OpenApiConfigurer;
import Mua.Mua_backend.global.swagger.SwaggerErrorExampleGenerator;
import Mua.Mua_backend.global.swagger.SwaggerOperationCustomizer;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${spring.swagger.url}")
    private String baseUrl;

    @Bean
    public OpenAPI openAPI() { return new OpenApiConfigurer(baseUrl).createOpenAPI(); }

    @Bean
    public SwaggerErrorExampleGenerator swaggerErrorExampleGenerator() {
        return new SwaggerErrorExampleGenerator();
    }

    @Bean
    public OperationCustomizer swaggerOperationCustomizer(SwaggerErrorExampleGenerator generator) {
        return new SwaggerOperationCustomizer(generator);
    }
}
