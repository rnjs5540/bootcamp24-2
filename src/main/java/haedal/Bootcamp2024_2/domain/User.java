package haedal.Bootcamp2024_2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Setter
    private String username;

    @Column(nullable = false)
    @Setter
    private String password;

    @Column(nullable = false)
    @Setter
    private String name;

    @Lob  // Large OBject의 줄임말. blob: binary lob
    @Column(name = "user_Image")
    @Setter
    private byte[] userImage;

    @Column(length = 500)
    @Setter
    private String bio;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;


    @OneToMany(mappedBy = "user")
    @JsonIgnore // 직렬화 시 무시
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    @JsonIgnore // 직렬화 시 무시
    private List<Like> likes;

    @OneToMany(mappedBy = "follower")
    @JsonIgnore // 직렬화 시 무시
    private List<Follow> followings;

    @OneToMany(mappedBy = "following")
    @JsonIgnore // 직렬화 시 무시
    private List<Follow> followers;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.userImage = null;
        this.bio = null;
        this.joinedAt = LocalDateTime.now();
    }
}
