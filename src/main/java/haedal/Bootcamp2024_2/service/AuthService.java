package haedal.Bootcamp2024_2.service;

import haedal.Bootcamp2024_2.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            throw new IllegalStateException("Unauthorized");
        }
        return (User) session.getAttribute("user");
    }
}
