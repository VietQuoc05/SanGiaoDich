package com.ecommerce.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FileController {

    private final FileService fileService;

    // 🔥 Upload 1 ảnh
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return fileService.upload(file);
    }

    // 🔥 Upload nhiều ảnh
    @PostMapping("/upload-multiple")
    public List<String> uploadMultiple(@RequestParam("files") List<MultipartFile> files) {
        return fileService.uploadMultiple(files);
    }
}