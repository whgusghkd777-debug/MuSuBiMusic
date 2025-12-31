package com.mysite.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(UserRole.USER); // デフォルト権限設定
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new RuntimeException("siteuser not found");
        }
    }

    @Transactional
    public void modify(SiteUser user, String email, String password) {
        SiteUser siteUser = this.userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        siteUser.setEmail(email);
        if (password != null && !password.isEmpty()) {
            siteUser.setPassword(passwordEncoder.encode(password));
        }
        this.userRepository.save(siteUser);
    }
}