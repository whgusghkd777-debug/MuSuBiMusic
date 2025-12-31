package com.mysite.sbb;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRepository;
import com.mysite.sbb.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        /* * 既存のユーザーを確認する際、DBの 'ADMIN' 文字列を 
         * UserRole.ADMIN 列挙型にマッピングしようとします。
         */
        Optional<SiteUser> _admin = this.userRepository.findByUsername("admin");
        
        if (_admin.isEmpty()) {
            SiteUser admin = new SiteUser();
            admin.setUsername("admin");
            admin.setEmail("admin@musicjoe.com");
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setRole(UserRole.ADMIN);
            this.userRepository.save(admin);
            System.out.println("=== 管理者アカウント(admin)が正常に生成されました ===");
        }
    }
}