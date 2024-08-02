package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.service.AuthService;
import haedal.Bootcamp2024_2.service.FollowService;
import haedal.Bootcamp2024_2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class FollowController {

    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;

    @PostMapping("/follows/{followingId}")
    public ResponseEntity<Void> followUser(@PathVariable Long followingId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        followService.followUser(currentUser.getId(), followingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/follows/{followingId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long followingId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        followService.unfollowUser(currentUser.getId(), followingId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/follows/{userId}/following")
    public ResponseEntity<List<UserSimpleResponseDto>> getFollowingUsers(@PathVariable Long userId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        List<UserSimpleResponseDto> followingUsers = followService.getFollowingUsers(currentUser.getId(), userId);
        return ResponseEntity.ok(followingUsers);
    }

    @GetMapping("/follows/{userId}/follower")
    public ResponseEntity<List<UserSimpleResponseDto>> getFollowerUsers(@PathVariable Long userId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        List<UserSimpleResponseDto> followerUsers = followService.getFollowerUsers(currentUser.getId(), userId);
        return ResponseEntity.ok(followerUsers);
    }
}
