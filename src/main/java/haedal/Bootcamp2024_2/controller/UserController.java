package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.dto.response.PostResponseDto;
import haedal.Bootcamp2024_2.dto.response.UserDetailResponseDto;
import haedal.Bootcamp2024_2.service.PostService;
import haedal.Bootcamp2024_2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

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