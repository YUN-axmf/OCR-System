package com.yun.backend.config;

import com.yun.backend.interceptor.JwtValidateInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

//@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {
    @Resource
    private JwtValidateInterceptor interceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(interceptor);
        registration.addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/user/info",
                        "/user/logout",
                        "/error",
                        "/upload",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/**"
                );

    }
}
