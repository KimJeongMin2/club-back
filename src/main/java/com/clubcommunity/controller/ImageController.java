package com.clubcommunity.controller;

import com.clubcommunity.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("upload") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.createDirectories(path.getParent()); // 경로 생성
            Files.write(path, bytes);

            return ResponseEntity.ok("/api/images/" + file.getOriginalFilename());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error uploading image", e);
        }
    }
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam("upload") MultipartFile file) {
//        String imageUrl = imageService.uploadImage(file);
//        return ResponseEntity.ok(imageUrl);
//    }


//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam("upload") MultipartFile file) {
//        String imageUrl = imageService.uploadImage(file);
//        return ResponseEntity.ok(imageUrl);
//    }
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadImage(@RequestParam("upload") MultipartFile file) {
//        String imageUrl = imageService.uploadImage(file);
//        return ResponseEntity.ok(imageUrl);
//    }
}
