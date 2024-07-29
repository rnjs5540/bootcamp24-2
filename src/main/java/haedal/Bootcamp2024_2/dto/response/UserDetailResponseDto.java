package haedal.Bootcamp2024_2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserDetailResponseDto {
    private Long id;
    private String username;
    private String name;
    private byte[] userImage;
    private String bio;
    private LocalDateTime joinedAt;
    private Long postCount;
    private Long followerCount;
    private Long followingCount;
}
