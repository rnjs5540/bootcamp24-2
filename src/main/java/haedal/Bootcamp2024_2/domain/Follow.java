package haedal.Bootcamp2024_2.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="follwer_id",nullable = false)
    private User follower;


    @ManyToOne
    @JoinColumn(name = "following_id",nullable = false)
    private User following;


    public Follow(User follwer, User following) {
        this.follower = follwer;
        this.following = following;
    }
}