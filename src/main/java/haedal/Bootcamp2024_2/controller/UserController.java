package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.request.UserUpdateRequestDto;
import haedal.Bootcamp2024_2.dto.response.UserDetailResponseDto;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.service.AuthService;
import haedal.Bootcamp2024_2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }


    @GetMapping("/users")
    public ResponseEntity<List<UserSimpleResponseDto>> getUsers(@RequestParam(required = false) String username, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        if (username == null || username.isEmpty()) {
            List<UserSimpleResponseDto> users = userService.getAllUsers(currentUser);
            return ResponseEntity.ok(users);
        }
        else {
            List<UserSimpleResponseDto> user = new ArrayList<>();
            UserSimpleResponseDto userSimpleResponseDto = userService.getUserSimpleByUsername(currentUser, username);
            if (userSimpleResponseDto != null) {
                user.add(userSimpleResponseDto);
            }
            return ResponseEntity.ok(user);
        }
    }

//    @GetMapping("/users")
//    public ResponseEntity<List<UserSimpleResponseDto>> getAllUsers(HttpServletRequest request) {
//        User currentUser = authService.getCurrentUser(request);
//        List<UserSimpleResponseDto> users = userService.getAllUsers(currentUser);
//
//        return ResponseEntity.ok(users);
//    }


    @PutMapping("/users/profile")
    public ResponseEntity<UserDetailResponseDto> updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        UserDetailResponseDto updated = userService.updateUser(currentUser, userUpdateRequestDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/users/{userId}/profile")
    public ResponseEntity<UserDetailResponseDto> getUserProfile(@PathVariable Long userId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        UserDetailResponseDto userDetailResponseDto = userService.getUserDetail(currentUser, userId);

        return ResponseEntity.ok(userDetailResponseDto);
    }

//    // 검색할때
////    @GetMapping("/users/username/{username}/profile")
//    @GetMapping("/users?username=")
//    public ResponseEntity<UserDetailResponseDto> getUserProfileByUsername(@PathVariable String username, HttpServletRequest request) {
//        User currentUser = authService.getCurrentUser(request);
//
//        UserDetailResponseDto userDetailResponseDto = userService.getUserDetailByUsername(currentUser, username);
//
//        return ResponseEntity.ok(userDetailResponseDto);
//    }
}