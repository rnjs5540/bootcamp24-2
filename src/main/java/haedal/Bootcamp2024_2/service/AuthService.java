package haedal.Bootcamp2024_2.service;

import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.request.LoginRequestDto;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    UserService userService;
    @Autowired
    private UserRepository userRepository;


    public User getCurrentUser(HttpServletRequest request) {
        System.out.println();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            throw new IllegalStateException("로그인되지 않았습니다.");  // unAuthorized
        }
        return (User) session.getAttribute("user");
    }

    public UserSimpleResponseDto login(LoginRequestDto loginRequestDto, HttpServletRequest request) {
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (!user.getPassword().equals(loginRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 잘못됐습니다.");
        }

        HttpSession session = request.getSession(); // session이 존재하지 않으면 새로운 세션 생성
        session.setAttribute("user", user);

        return userService.convertUserToSimpleDto(user, user);
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false를 전달하면, 현재 session이 존재하지 않으면 null을 반환
        if (session != null) {
            session.invalidate();
        }
    }
}
