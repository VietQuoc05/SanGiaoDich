package com.ecommerce.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // 🔥 PHẢI LÀ: ROLE_USER / ROLE_SUPPLIER / ROLE_ADMIN
    @Column(nullable = false)
    private String role;

    private String phone;

    private String gender;

    private Integer yearOfBirth;

    @Column(nullable = false)
    private boolean supplierRequest = false;
}