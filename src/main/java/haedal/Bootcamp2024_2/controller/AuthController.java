package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.request.LoginRequestDto;
import haedal.Bootcamp2024_2.dto.request.UserRegistrationRequestDto;
import haedal.Bootcamp2024_2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequestDto userRegistrationRequestDto) {
        User user = new User(
                userRegistrationRequestDto.getUsername(),
                userRegistrationRequestDto.getPassword(),
                userRegistrationRequestDto.getName(),
                userRegistrationRequestDto.getUserImage(),
                userRegistrationRequestDto.getBio()
        );
        userService.save(user);

        return ResponseEntity.ok("회원가입 성공");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        User user = userService.findByUsername(loginRequestDto.getUsername()).orElse(null);

        if (user != null && user.getPassword().equals(loginRequestDto.getPassword())) {
            HttpSession session = request.getSession(); // session이 존재하지 않으면 새로운 세션 생성
            session.setAttribute("user", user);
            return ResponseEntity.ok("로그인 성공"); // 쿠키에 세션아이디 담겨서 전송됨
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("username이나 password가 잘못됐습니다");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
            // false를 전달하면, 현재 세션이 존재하지 않을 때 새로운 세션을 생성하지 않고 null을 반환
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 성공");
    }


    @GetMapping("/me")
    public ResponseEntity<User> me(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}