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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "image_url")
    @Getter
    @Setter
    private String imageUrl;

    @Column(length = 2000)
    private String context;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post")
    private List<Like> likes;

    public Post(User user, String context, String imageUrl) {
        this.user = user;
        this.imageUrl = imageUrl;
        this.context = context;
        this.createdAt = LocalDateTime.now();
    }
}
