package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.service.AuthService;
import haedal.Bootcamp2024_2.service.ImageService;
import haedal.Bootcamp2024_2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageController {
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    ImageService imageService;

    @PostMapping("/images/postImages")
    public ResponseEntity<String> uploadPostImage(@RequestParam("image") MultipartFile image) throws IOException {
        String imageUrl = imageService.savePostImage(image);
        return ResponseEntity.ok(imageUrl);
    }

    @PutMapping("/users/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) throws IOException {
        User currentUser = authService.getCurrentUser(request);

        String savedImageName = imageService.updateUserImage(currentUser.getId(), image);
        return ResponseEntity.ok(savedImageName);
    }

    @GetMapping("/images/{imageUrl}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageUrl) {

        try {
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static";

            Path imagePath = Paths.get(uploadDir).resolve(imageUrl).normalize();
            Resource resource = new UrlResource(imagePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
