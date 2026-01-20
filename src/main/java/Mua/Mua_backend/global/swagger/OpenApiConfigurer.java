package Mua.Mua_backend.global.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

public class OpenApiConfigurer {

    private final String baseUrl;

    public OpenApiConfigurer(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public OpenAPI createOpenAPI() {
        return new OpenAPI()
                .info(createInfo())
                .addSecurityItem(createSecurityRequirement())
                .components(createComponents())
                .servers(createServerList());
    }

    private Info createInfo() {
        return new Info()
                .title("MuaCon API 문서")
                .version("v1.0.0")
                .description("MuaCon서비스 API 문서입니다.");
    }

    private SecurityRequirement createSecurityRequirement() {
        return new SecurityRequirement().addList("JWT");
    }

    private Components createComponents() {
        return new Components()
                .addSecuritySchemes("JWT",
                        new SecurityScheme()
                                .name("JWT")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT 토큰을 입력하세요.(Bearer XXX)"));
    }

    private List<Server> createServerList() {
        return List.of(
                new Server().description("Production Server").url(baseUrl),
                new Server().description("Local Server").url("http://localhost:8080")
        );
    }
}
