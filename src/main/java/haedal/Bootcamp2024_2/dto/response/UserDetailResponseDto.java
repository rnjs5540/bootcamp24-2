package haedal.Bootcamp2024_2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserDetailResponseDto {
    private Long id;
    private String username;
    private String name;
    private String imageUrl;
    private Boolean isFollowing;
    private String bio;
    private String joinedAt;
    private Long postCount;
    private Long followerCount;
    private Long followingCount;
}
