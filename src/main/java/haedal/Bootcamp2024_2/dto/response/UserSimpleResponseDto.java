package haedal.Bootcamp2024_2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSimpleResponseDto {
    private Long id;
    private String username;
    private byte[] userImage;
    private String name;
}
