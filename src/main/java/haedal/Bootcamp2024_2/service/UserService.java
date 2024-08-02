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

import java.time.format.DateTimeFormatter;
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


    public UserSimpleResponseDto saveUser(User user) {
        // 중복 회원 검증
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        userRepository.save(user);

        return convertUserToSimpleDto(user, user);
    }

    public List<UserSimpleResponseDto> getAllUsers(User currentUser) {
        List<User> users = userRepository.findAll();
        users.remove(currentUser);
        return users.stream().map(user -> convertUserToSimpleDto(currentUser, user)).toList();
    }

    public UserDetailResponseDto getUserDetail(Long currentUserId, Long userId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return convertUserToDetailDto(currentUser, user);
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

        return convertUserToDetailDto(user, user);
    }


    public UserDetailResponseDto convertUserToDetailDto(User currentUser, User targetUser) {
        System.out.println("Current User ID: " + currentUser.getId());
        System.out.println("Target User ID: " + targetUser.getId());

        return UserDetailResponseDto.builder()
                .id(targetUser.getId())
                .username(targetUser.getUsername())
                .name(targetUser.getName())
                .imageUrl(targetUser.getImageUrl())
                .bio(targetUser.getBio())
                .joinedAt(targetUser.getJoinedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")))
                .postCount(postRepository.countByUser(targetUser))
                .followerCount(followRepository.countByFollower(targetUser))
                .followingCount(followRepository.countByFollowing(targetUser))
                .isFollowing(followRepository.existsByFollowerAndFollowing(currentUser, targetUser))
                .build();
    }

    public UserSimpleResponseDto convertUserToSimpleDto(User currentUser, User targetUser) {
        return UserSimpleResponseDto.builder()
                .id(targetUser.getId())
                .username(targetUser.getUsername())
                .name(targetUser.getName())
                .imageUrl(targetUser.getImageUrl())
                .isFollowing(followRepository.existsByFollowerAndFollowing(currentUser, targetUser))
                .build();
    }
}

