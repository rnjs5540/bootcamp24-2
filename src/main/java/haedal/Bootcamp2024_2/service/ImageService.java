package haedal.Bootcamp2024_2.service;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ImageService {
    @Autowired
    UserRepository userRepository;

    private final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static";
        // System.getProperty("user.dir"): 현재작업디렉토리의 절대경로

    public String savePostImage(MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!uploadPath.toFile().exists()) {
            uploadPath.toFile().mkdirs();
        }

        // 현재 시간을 기준으로 고유한 이미지 이름 생성
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String imageUrl = "p" + timestamp + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(imageUrl);
        image.transferTo(filePath.toFile());

        // 파일 경로 반환 (경로는 필요한 대로 수정)
        return imageUrl;
    }

    public String updateUserImage(User user, MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!uploadPath.toFile().exists()) {
            uploadPath.toFile().mkdirs();
        }

        // 현재 시간을 기준으로 고유한 이미지 이름 생성
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String imageUrl = "u" + timestamp + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(imageUrl);
        image.transferTo(filePath.toFile());

        user.setImageUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    public Resource loadImageAsResource(String imageUrl) throws MalformedURLException {
        Path imagePath = Paths.get(uploadDir).resolve(imageUrl).normalize();
        Resource resource = new UrlResource(imagePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }
}
