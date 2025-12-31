package com.mysite.sbb.user;

import lombok.Getter;

/**
 * ユーザー権限を管理するEnum
 * DBのrole列の値（ADMIN, USER）と一致させています
 */
@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}