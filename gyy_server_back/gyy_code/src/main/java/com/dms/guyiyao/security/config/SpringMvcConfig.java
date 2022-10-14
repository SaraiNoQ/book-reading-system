package com.dms.guyiyao.security.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 访问路径配置类 可以理解成做简单访问过滤的，转发到相应的视图页面
 *
 * @author Veiking
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
/*
* 配置跨域问题
* */
    public void addCorsMappings(CorsRegistry registry){
registry.addMapping("/**")
        .allowedOriginPatterns("*")
        .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTIONS")
        .allowCredentials(true)
        .maxAge(3600)
        .allowedHeaders("*");
    }



}

