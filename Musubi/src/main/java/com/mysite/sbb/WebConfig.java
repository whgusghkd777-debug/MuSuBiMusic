package com.mysite.sbb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // パス区切り文字の互換性を確保
        String path = uploadPath.replace("\\", "/");
        if (!path.endsWith("/")) {
            path += "/";
        }

        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + path); 
    }
}