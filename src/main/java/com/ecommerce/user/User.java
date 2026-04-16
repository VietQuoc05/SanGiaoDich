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

    private String username;

    private String password;

    // ⚠️ DB đang là ROLE_USER
    private String role;

    // ===== NEW FIELDS =====
    private String phone;

    private String gender;

    private Integer yearOfBirth;

    // 🔥 FIX LỖI SQL
    @Column(nullable = false)
    private boolean supplierRequest = false;
}