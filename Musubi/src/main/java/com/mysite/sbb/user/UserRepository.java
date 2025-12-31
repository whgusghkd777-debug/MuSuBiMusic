package com.mysite.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    // Serviceで使用する名前と完全に一致させる必要があります
    Optional<SiteUser> findByUsername(String username);
}