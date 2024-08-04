package haedal.Bootcamp2024_2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PostResponseDto {
    private Long id;
    private UserSimpleResponseDto user;
    private String imageUrl;
    private String content;
    private Long likeCount;
    private Boolean islike;
    private String createdAt;
}
