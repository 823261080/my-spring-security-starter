package com.jwzhang.starter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * swagger配置
 */
@Configuration
public class SwaggerConfig {

    @Value("${swagger.enable: true}")
    private boolean swaggerEnable;

    private String version = "1.0";

    /**
     * swagger配置 - swagger访问路径 http://127.0.0.1:8022/swagger-ui/index.html
     *
     * @return {@link Docket}
     */
    @Bean
    public Docket createRestApi() {
        // v2 不同
        return new Docket(DocumentationType.OAS_30)
                .groupName("默认")
                .apiInfo(apiInfo())
                .enable(swaggerEnable)
                .securitySchemes(Collections.singletonList(tokenScheme()))
                .securityContexts(Collections.singletonList(tokenContext()))
                .select()
                // 设置扫描路径
                .apis(RequestHandlerSelectors.basePackage("com.jwzhang.starter.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("接口文档")
                // 版本号
                .version(version)
                // 描述
                .description("接口文档")
                .build();
    }

    private HttpAuthenticationScheme tokenScheme() {
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER
                .name("Authorization")
                .build();
    }

    private SecurityContext tokenContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(SecurityReference.builder()
                        .scopes(new AuthorizationScope[0])
                        .reference("Authorization")
                        .build()))
                .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
                .build();
    }
}