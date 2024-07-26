package haedal.Bootcamp2024_2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob  // Large OBject의 줄임말. blob: binary lob
    private byte[] image;

    @Column(length = 2000)
    private String context;

    @Column(name = "like_count")
    @Setter
    private Integer likeCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post")
    @JsonIgnore // 직렬화 시 무시
    private List<Like> likes;

    public Post(User user, byte[] image, String context) {
        this.user = user;
        this.image = image;
        this.context = context;
        this.likeCount = 0;
        this.createdAt = LocalDateTime.now();
    }
}
