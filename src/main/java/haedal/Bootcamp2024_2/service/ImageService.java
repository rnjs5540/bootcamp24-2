package haedal.Bootcamp2024_2.service;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class ImageService {
    private final UserRepository userRepository;
    private final Path uploadDir = Paths.get(System.getProperty("user.dir"), "src/main/resources/static");
    // System.getProperty("user.dir"): 현재작업디렉토리의 절대경로를 가져옴.

    @Autowired
    public ImageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String updateUserImage(User user, MultipartFile image) throws IOException {
        // 현재 시간을 기준으로 고유한 이미지 이름 생성
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String uniqueImageName = timestamp + "_" + image.getOriginalFilename();

        Path filePath = uploadDir.resolve("userImages").resolve(uniqueImageName);
        Files.createDirectories(filePath.getParent());  // 경로폴더 없으면 생성
        image.transferTo(filePath.toFile());

        user.setImageUrl("userImages/" + uniqueImageName);
        userRepository.save(user);

        return "userImages/" + uniqueImageName;
    }

    public String encodeImageToBase64(String imagePath) {
        try {
            Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            return null;
        }
    }

    public String savePostImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("비어있는 이미지 파일입니다..");
        }

        // 현재 시간을 기준으로 고유한 이미지 이름 생성
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String uniqueImageName = timestamp + "_" + image.getOriginalFilename();

        Path filePath = uploadDir.resolve("postImages").resolve(uniqueImageName);
        Files.createDirectories(filePath.getParent());  // 경로폴더 없으면 생성
        image.transferTo(filePath.toFile());

        return "postImages/" + uniqueImageName;  // 상대 경로 반환
    }
}
