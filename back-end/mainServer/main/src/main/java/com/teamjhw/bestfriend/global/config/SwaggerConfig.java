package com.teamjhw.bestfriend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Collections;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("BestFriend API 명세서")
                .description(
                        "<h2>SSAFY 11기 2학기 자율 프로젝트</h2>" +
                        "<img src=\"/images/bf_logo.png\" alt = '로고'  width=\"120\">")
                .version("1.0.0")
                ;

        // JWT 보안 스키마 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // 전역 보안 요구사항 정의
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Collections.singletonList(securityRequirement))
                .info(info);
    }

    // ! Member domain
    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
                             .group("member & auth")
                             .pathsToMatch("/bf/auth/**",
                                           "/bf/member/**")
                             .build();
    }

    // ! Character domain
    @Bean
    public GroupedOpenApi characterApi() {
        return GroupedOpenApi.builder()
                .group("character")
                .pathsToMatch("/bf/character/**")
                             .build();
    }

    // ! Match domain
    @Bean
    public GroupedOpenApi matchApi() {
        return GroupedOpenApi.builder()
                             .group("match")
                             .pathsToMatch("/bf/match/**")
                             .build();
    }

    // ! Game domain
    @Bean
    public GroupedOpenApi gameApi() {
        return GroupedOpenApi.builder()
                             .group("game")
                             .pathsToMatch("/bf/game/**")
                             .build();
    }

    // ! News domain
    @Bean
    public GroupedOpenApi newsApi() {
        return GroupedOpenApi.builder()
                             .group("news")
                             .pathsToMatch("/bf/news/**")
                             .build();
    }
}