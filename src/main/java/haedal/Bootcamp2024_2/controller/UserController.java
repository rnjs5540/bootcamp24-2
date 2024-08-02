package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.request.UserUpdateRequestDto;
import haedal.Bootcamp2024_2.dto.response.PostResponseDto;
import haedal.Bootcamp2024_2.dto.response.UserDetailResponseDto;
import haedal.Bootcamp2024_2.service.AuthService;
import haedal.Bootcamp2024_2.service.PostService;
import haedal.Bootcamp2024_2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class UserController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;


    @PutMapping("/users/profile")
    public ResponseEntity<UserDetailResponseDto> updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        UserDetailResponseDto updatedUser = userService.updateUser(currentUser.getId(), userUpdateRequestDto);
        return ResponseEntity.ok(updatedUser);
    }


    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<UserDetailResponseDto> getUserDetail(@PathVariable Long userId) {
        UserDetailResponseDto userDetailResponseDto = userService.getUserDetail(userId);
        return ResponseEntity.ok(userDetailResponseDto);
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<Page<PostResponseDto>> getPostsByUser(@PathVariable Long userId, Pageable pageable) {
        Page<PostResponseDto> posts = postService.getPostsByUser(userId, pageable);
        return ResponseEntity.ok(posts);
    }
}