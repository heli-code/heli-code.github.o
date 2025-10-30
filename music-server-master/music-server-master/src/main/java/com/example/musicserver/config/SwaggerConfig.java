package com.example.musicserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2//
public class SwaggerConfig{

    @Bean
    public Docket docket(){

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.example.musicserver.controller")).build();
    }


    private ApiInfo apiInfo(){
        Contact contact=new Contact("wowo","http://localhost:8081/","979391150@qq.com");

        return new ApiInfo(
                "Liwz的API文档",
                "这是用来开发员工管理系统的API文档",
                "v1.0",
                "http://www.liwz.top",
                contact,"Apache 2.0",
                "http://www.liwz.top",
                new ArrayList<>()
                 );
    }

}
