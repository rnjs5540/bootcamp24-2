package haedal.Bootcamp2024_2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private UserSimpleResponseDto user;
    private byte[] image;
    private String context;
    private Long likeCount;
    private boolean islike;
    private String createdAt;
}
