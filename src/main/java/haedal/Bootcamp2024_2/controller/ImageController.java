package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.service.AuthService;
import haedal.Bootcamp2024_2.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
public class ImageController {
    @Autowired
    private AuthService authService;
    @Autowired
    private ImageService imageService;

    @PostMapping("/images/postImages")
    public ResponseEntity<String> uploadPostImage(@RequestParam("image") MultipartFile image) throws IOException {
        String imageUrl = imageService.savePostImage(image);
        return ResponseEntity.ok(imageUrl);
    }

    @PutMapping("/users/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) throws IOException {
        User currentUser = authService.getCurrentUser(request);

        String savedImageName = imageService.updateUserImage(currentUser, image);
        return ResponseEntity.ok(savedImageName);
    }

    @GetMapping("/images/{imageUrl}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageUrl) throws MalformedURLException {
        Resource resource = imageService.loadImageAsResource(imageUrl);
        return ResponseEntity.ok(resource);
    }
}