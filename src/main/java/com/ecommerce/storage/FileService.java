package com.ecommerce.storage;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;

    private final String BUCKET_NAME = "products";

    // 🔥 Upload 1 file
    public String upload(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return "http://localhost:9000/" + BUCKET_NAME + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Upload failed", e);
        }
    }

    // 🔥 Upload nhiều file
    public List<String> uploadMultiple(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            urls.add(upload(file));
        }

        return urls;
    }
}