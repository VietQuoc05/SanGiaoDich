package com.ecommerce.product;

import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.storage.FileService;
import com.ecommerce.user.User;
import com.ecommerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final FileService fileService;

    // ================= CREATE =================
    public ProductResponse create(
            String name,
            String description,
            Double price,
            List<MultipartFile> files,
            String username
    ) {
        User supplier = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .supplier(supplier)
                .build();

        // 🔥 upload ảnh
        if (files != null && !files.isEmpty()) {
            List<String> urls = fileService.uploadMultiple(files);

            for (String url : urls) {
                ProductImage image = ProductImage.builder()
                        .url(url)
                        .build();

                product.addImage(image); // 🔥 dùng helper
            }
        }

        productRepo.save(product);

        return mapToResponse(product);
    }

    // ================= UPDATE =================
    public ProductResponse update(
            Long id,
            String name,
            String description,
            Double price,
            List<MultipartFile> files,
            String username
    ) {
        Product product = productRepo.findByIdWithImages(id);

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        if (!product.getSupplier().getUsername().equals(username)) {
            throw new RuntimeException("Not owner");
        }

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        // 🔥 update ảnh
        if (files != null && !files.isEmpty()) {

            product.getImages().clear();

            List<String> urls = fileService.uploadMultiple(files);

            for (String url : urls) {
                ProductImage image = ProductImage.builder()
                        .url(url)
                        .build();

                product.addImage(image); // 🔥 luôn dùng helper
            }
        }

        productRepo.save(product);

        return mapToResponse(product);
    }

    // ================= DELETE =================
    public void delete(Long id, String username) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        if (!product.getSupplier().getUsername().equals(username)) {
            throw new RuntimeException("Not owner");
        }

        productRepo.delete(product);
    }

    // ================= GET =================
    public List<ProductResponse> getAll() {
        return productRepo.findAllWithImages()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse getById(Long id) {
        return mapToResponse(productRepo.findByIdWithImages(id));
    }

    public List<ProductResponse> search(String keyword) {
        return productRepo.searchWithImages(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ProductResponse> getMyProducts(String username) {
        return productRepo.findBySupplierUsernameWithImages(username)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= MAPPER =================
    private ProductResponse mapToResponse(Product product) {

        List<String> images = new ArrayList<>();

        if (product.getImages() != null) {
            for (ProductImage img : product.getImages()) {
                images.add(img.getUrl());
            }
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrls(images)
                .supplierName(product.getSupplier().getUsername())
                .build();
    }
}