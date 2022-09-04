package com.jwzhang.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;
//swagger3注解
@EnableOpenApi
@SpringBootApplication
public class SecurityStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityStarterApplication.class, args);
    }

}
