package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.service.AuthService;
import haedal.Bootcamp2024_2.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class ImageController {
    private final ImageService imageService;
    private final AuthService authService;

    @Autowired
    public ImageController(ImageService imageService, AuthService authService) {
        this.imageService = imageService;
        this.authService = authService;
    }

    @PutMapping("/users/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) throws IOException {
        User currentUser =authService.getCurrentUser(request);


        String savedImageName = imageService.updateUserImage(currentUser,image);
        return ResponseEntity.ok(savedImageName);
    }

}