package com.mysite.sbb.music.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @GetMapping("/api/current-user")
    public Map<String, Object> currentUser(Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            response.put("username", auth.getName());
            response.put("isLoggedIn", true);
            
            // ADMIN 역할 있는지 확인
            boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));
            response.put("isAdmin", isAdmin);
        } else {
            response.put("username", null);
            response.put("isLoggedIn", false);
            response.put("isAdmin", false);
        }
        
        return response;
    }
}