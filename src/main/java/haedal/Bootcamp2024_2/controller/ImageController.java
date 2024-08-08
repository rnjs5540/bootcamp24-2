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
    private final AuthService authService;
    private final ImageService imageService;

    @Autowired
    public ImageController(AuthService authService, ImageService imageService) {
        this.authService = authService;
        this.imageService = imageService;
    }

    @PutMapping("/users/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) throws IOException {
        User currentUser = authService.getCurrentUser(request);

        String savedImageName = imageService.updateUserImage(currentUser, image);
        return ResponseEntity.ok(savedImageName);
    }

//    @GetMapping("/images/{imageUrl}")
//    public ResponseEntity<Resource> getImage(@PathVariable String imageUrl) throws IOException {
//        Resource resource = imageService.loadImageAsResource(imageUrl);
//        Path imagePath = resource.getFile().toPath();
//        String contentType = Files.probeContentType(imagePath);
//
//        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
//    }
}