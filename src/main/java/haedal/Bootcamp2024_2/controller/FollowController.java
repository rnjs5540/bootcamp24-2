package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.service.AuthService;
import haedal.Bootcamp2024_2.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FollowController {
    private final FollowService follService;
    private final AuthService authService;
    private final FollowService followService;

    @Autowired
    public FollowController(FollowService follService, AuthService authService, FollowService followService) {
        this.follService = follService;
        this.authService = authService;
        this.followService = followService;
    }

    @PostMapping("/follows/{followingId}")
    public ResponseEntity<Void> followUSer(@PathVariable long followingId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        followService.followUser(currentUser,followingId);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/follows/{followingId}")
    public ResponseEntity<Void> unfollowUSer(@PathVariable long followingId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        followService.unfollowUser(currentUser,followingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/follows/{userId}/following")
    public ResponseEntity<List<UserSimpleResponseDto>> getFollowingUsers(@PathVariable long userId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        List<UserSimpleResponseDto> followingUsers =followService.getFollowingUsers(currentUser,userId);
        return ResponseEntity.ok(followingUsers);
    }

    @GetMapping("/follows/{userId}/follower")
    public ResponseEntity<List<UserSimpleResponseDto>> getFollowerUsers(@PathVariable Long userId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        List<UserSimpleResponseDto> followerUsers = followService.getFollowerUsers(currentUser, userId);
        return ResponseEntity.ok(followerUsers);
    }



}