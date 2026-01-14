package com.mysite.sbb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    
    //役割（Role）に基づいた権限管理
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http  
            .authorizeHttpRequests((authorize) -> authorize
            // 静的リソース（CSS/JS）やアップロードファイルへのアクセスを許可
                .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                // 管理者（ADMIN）のみ、管理画面および楽曲の削除権限を付与
                .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/upload/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/music/delete/**")).authenticated()
                // その他の一般ページは全ユーザーに公開
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
            
            .csrf((csrf) -> csrf.disable())
            .headers((headers) -> headers.frameOptions((f) -> f.sameOrigin()))
            
            .formLogin((login) -> login
                .loginPage("/user/login")
                .defaultSuccessUrl("/music/list")) // /list -> /music/list に修正
                
            
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/music/list") // /list -> /music/list に修正
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"));
        
        return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

