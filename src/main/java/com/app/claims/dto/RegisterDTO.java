package com.app.claims.dto;

import com.app.claims.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterDTO {

    @NotNull(message = "Username is required")
    @NotBlank
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Username is required")
    @NotBlank
    private String password;

    private Role role;
}
