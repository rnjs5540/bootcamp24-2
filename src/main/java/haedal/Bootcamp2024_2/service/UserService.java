package haedal.Bootcamp2024_2.service;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.request.UserUpdateRequestDto;
import haedal.Bootcamp2024_2.dto.response.UserDetailResponseDto;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.repository.FollowRepository;
import haedal.Bootcamp2024_2.repository.PostRepository;
import haedal.Bootcamp2024_2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

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

    public void updateUserImage(Long userId, byte[] userImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        user.setUserImage(userImage);
        userRepository.save(user);
    }

    public UserDetailResponseDto getUserDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        return new UserDetailResponseDto(
                userId,
                user.getUsername(),
                user.getName(),
                user.getUserImage(),
                user.getBio(),
                user.getJoinedAt(),
                postRepository.countByUser(user),
                followRepository.countByFollowing(user),
                followRepository.countByFollower(user)
        );
    }

    public UserSimpleResponseDto getUserSimple(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new IllegalStateException("유저가 존재하지 않습니다.");
        }

        return new UserSimpleResponseDto(
                id,
                user.getUsername(),
                user.getUserImage(),
                user.getName()
        );
    }
}

