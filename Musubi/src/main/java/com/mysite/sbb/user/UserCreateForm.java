package com.mysite.sbb.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "ユーザー名は必須です。")
    private String username;

    @NotEmpty(message = "パスワードは必須です。")
    private String password1;

    @NotEmpty(message = "パスワード確認は必須です。")
    private String password2;

    @NotEmpty(message = "メールアドレスは必須です。")
    @Email
    private String email;
}