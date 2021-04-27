package com.inzent.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO Auto-generated method stub
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**").excludePathPatterns("/login")
                .excludePathPatterns("/logout").excludePathPatterns("/assets/**");
        // WebMvcConfigurer.super.addInterceptors(registry);
    }
}