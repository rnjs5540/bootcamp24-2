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

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private ImageService imageService;


    public UserSimpleResponseDto saveUser(User newUser) {
        // 중복 회원 검증
        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new IllegalStateException("중복되는 username입니다.");
        }

        userRepository.save(newUser);
        return convertUserToSimpleDto(newUser, newUser);
    }

//    public UserSimpleResponseDto findUserById(Long id) {
//        User user = userRepository.findById(id).orElse(null);
//        if (user == null) {
//            return null;
//        }
//
//        return convertUserToSimpleDto(user, user);
//    }

    public List<UserSimpleResponseDto> getAllUsers(User currentUser) {
        List<User> users = userRepository.findAll();
        users.remove(currentUser);
        return users.stream().map(user -> convertUserToSimpleDto(currentUser, user)).toList();
    }

    public UserDetailResponseDto getUserDetail(User currentUser, Long targetUserId) {
       User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return convertUserToDetailDto(currentUser, targetUser);
    }


    public UserSimpleResponseDto getUserSimpleByUsername(User currentUser, String username) {
        User targetUser = userRepository.findByUsername(username).orElse(null);
        if (targetUser == null) {
            return null;
        }

        return convertUserToSimpleDto(currentUser, targetUser);
    }


    public UserDetailResponseDto updateUser(User currentUser, UserUpdateRequestDto userUpdateRequestDto) {
        if (userUpdateRequestDto.getUsername() != null) {
            currentUser.setUsername(userUpdateRequestDto.getUsername());
        }
        if (userUpdateRequestDto.getPassword() != null) {
            currentUser.setPassword(userUpdateRequestDto.getPassword());
        }
        if (userUpdateRequestDto.getName() != null) {
            currentUser.setName(userUpdateRequestDto.getName());
        }
        if (userUpdateRequestDto.getBio() != null) {
            currentUser.setBio(userUpdateRequestDto.getBio());
        }

        userRepository.save(currentUser);

        return convertUserToDetailDto(currentUser, currentUser);
    }



    public UserSimpleResponseDto convertUserToSimpleDto(User currentUser, User targetUser) {
        String imageUrl = currentUser.getImageUrl();
        String imageData = imageService.encodeImageToBase64(System.getProperty("user.dir") + "/src/main/resources/static/" + imageUrl);

        return UserSimpleResponseDto.builder()
                .id(targetUser.getId())
                .username(targetUser.getUsername())
                .name(targetUser.getName())
                .imageData(imageData)
                .isFollowing(followRepository.existsByFollowerAndFollowing(currentUser, targetUser))
                .build();
    }

    public UserDetailResponseDto convertUserToDetailDto(User currentUser, User targetUser) {
        String imageUrl = currentUser.getImageUrl();
        String imageData = imageService.encodeImageToBase64(System.getProperty("user.dir") + "/src/main/resources/static/" + imageUrl);


        return UserDetailResponseDto.builder()
                .id(targetUser.getId())
                .username(targetUser.getUsername())
                .name(targetUser.getName())
                .imageData(imageData)
                .bio(targetUser.getBio())
                .joinedAt(targetUser.getJoinedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")))
                .postCount(postRepository.countByUser(targetUser))
                .followerCount(followRepository.countByFollowing(targetUser))
                .followingCount(followRepository.countByFollower(targetUser))
                .isFollowing(followRepository.existsByFollowerAndFollowing(currentUser, targetUser))
                .build();
    }
}

