package haedal.Bootcamp2024_2.service;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.request.UserUpdateRequestDto;
import haedal.Bootcamp2024_2.dto.response.UserDetailResponseDto;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.repository.FollowRepository;
import haedal.Bootcamp2024_2.repository.PostRepository;
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
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FollowRepository followRepository;


    public UserSimpleResponseDto save(User user) {
        // 중복 회원 검증
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        userRepository.save(user);

        return getUserSimple(user.getId());
    }


    public UserDetailResponseDto updateUser(Long userId, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (userUpdateRequestDto.getUsername() != null) {
            user.setUsername(userUpdateRequestDto.getUsername());
        }
        if (userUpdateRequestDto.getPassword() != null) {
            user.setPassword(userUpdateRequestDto.getPassword());
        }
        if (userUpdateRequestDto.getName() != null) {
            user.setName(userUpdateRequestDto.getName());
        }
        if (userUpdateRequestDto.getBio() != null) {
            user.setBio(userUpdateRequestDto.getBio());
        }

        userRepository.save(user);

        return getUserDetail(user.getId());
    }


    public String updateImage(Long userId, MultipartFile image) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/userImages";
        Path uploadPath = Paths.get(uploadDir);
        if (!uploadPath.toFile().exists()) {
            uploadPath.toFile().mkdirs();
        }

        // 현재 시간을 기준으로 고유한 이미지 이름 생성
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String imageName = timestamp + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(imageName);

        image.transferTo(filePath.toFile());

        // 파일 이름만 설정
        user.setUserImage(imageName);
        userRepository.save(user);

        return imageName;
    }


    public UserDetailResponseDto getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        String joinedAt = user.getJoinedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));

        return new UserDetailResponseDto(
                userId,
                user.getUsername(),
                user.getName(),
                user.getUserImage(),
                user.getBio(),
                joinedAt,
                postRepository.countByUser(user),
                followRepository.countByFollowing(user),
                followRepository.countByFollower(user)
        );
    }

    public UserSimpleResponseDto getUserSimple(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        return new UserSimpleResponseDto(
                userId,
                user.getUsername(),
                user.getUserImage(),
                user.getName()
        );
    }
}

