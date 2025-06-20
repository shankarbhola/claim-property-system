package com.app.claims.entity;

import com.app.claims.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username is required")
    @Column(unique = true)
    private String username;

    @NotNull(message = "Password is required")
    private String password;

    @Email(message = "Invalid email")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

}