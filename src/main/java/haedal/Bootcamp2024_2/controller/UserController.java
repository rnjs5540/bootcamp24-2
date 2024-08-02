package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.request.UserUpdateRequestDto;
import haedal.Bootcamp2024_2.dto.response.PostResponseDto;
import haedal.Bootcamp2024_2.dto.response.UserDetailResponseDto;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.service.AuthService;
import haedal.Bootcamp2024_2.service.PostService;
import haedal.Bootcamp2024_2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping
public class UserController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;


    @GetMapping("/users")
    public ResponseEntity<List<UserSimpleResponseDto>> getAllUsers(HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        List<UserSimpleResponseDto> users = userService.getAllUsers(currentUser);

        return ResponseEntity.ok(users);
    }


    @PutMapping("/users/profile")
    public ResponseEntity<UserDetailResponseDto> updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        UserDetailResponseDto updated = userService.updateUser(currentUser.getId(), userUpdateRequestDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<UserDetailResponseDto> getUserProfile(@PathVariable Long userId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        UserDetailResponseDto userDetailResponseDto = userService.getUserDetail(currentUser.getId(), userId);

        return ResponseEntity.ok(userDetailResponseDto);
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<List<PostResponseDto>> getPostsByUser(@PathVariable Long userId) {
        List<PostResponseDto> posts = postService.getPostsByUser(userId);
        return ResponseEntity.ok(posts);
    }
}