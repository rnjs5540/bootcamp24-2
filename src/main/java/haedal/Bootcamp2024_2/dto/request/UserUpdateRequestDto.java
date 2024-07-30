package haedal.Bootcamp2024_2.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    private String username;
    private String password;
    private String name;
    private String bio;
}