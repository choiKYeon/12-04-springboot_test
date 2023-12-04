package com.std.sbb.user.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @NotEmpty(message = "ID는 필수입니다.")
    @Size(min = 3, max = 20)
    private String username;

    @NotEmpty(message = "패스워드는 필수입니다.")
    private String password1;

    @NotEmpty(message = "패스워드 확인은 필수입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수입니다.")
    @Email
    private String email;
}
