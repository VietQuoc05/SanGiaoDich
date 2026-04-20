package com.ecommerce.product;

import com.ecommerce.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private User supplier;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductImage> images;

    // 🔥 FIX NPE khi dùng builder
    @PrePersist
    public void prePersist() {
        if (images == null) {
            images = new ArrayList<>();
        }
    }

    // 🔥 helper chuẩn (RẤT QUAN TRỌNG)
    public void addImage(ProductImage image) {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(image);
        image.setProduct(this);
    }
}