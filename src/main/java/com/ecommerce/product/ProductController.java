package com.ecommerce.product;

import com.ecommerce.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    // 🔥 CREATE (upload ảnh trực tiếp)
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ProductResponse create(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam("files") List<MultipartFile> files,
            Authentication auth
    ) {
        return productService.create(
                name,
                description,
                price,
                files,
                auth.getName()
        );
    }

    // 🔥 GET ALL
    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }

    // 🔥 GET BY ID
    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // 🔥 SEARCH
    @GetMapping("/search")
    public List<ProductResponse> search(@RequestParam String keyword) {
        return productService.search(keyword);
    }

    // 🔥 MY PRODUCTS
    @GetMapping("/my")
    public List<ProductResponse> my(Authentication auth) {
        return productService.getMyProducts(auth.getName());
    }

    // 🔥 UPDATE (có thể update ảnh)
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ProductResponse update(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            Authentication auth
    ) {
        return productService.update(
                id,
                name,
                description,
                price,
                files,
                auth.getName()
        );
    }

    // 🔥 DELETE
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Authentication auth) {
        productService.delete(id, auth.getName());
        return "Deleted";
    }
}