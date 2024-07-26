package haedal.Bootcamp2024_2.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRegistrationRequestDto {
    private String username;
    private String password;
    private String name;
    private String bio;
    private byte[] userImage;
}