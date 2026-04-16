package com.ecommerce.supplier;

import com.ecommerce.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "supplier_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // user gửi request
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // PENDING / APPROVED / REJECTED
    private String status;
}