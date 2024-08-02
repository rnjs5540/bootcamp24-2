package haedal.Bootcamp2024_2;
import haedal.Bootcamp2024_2.domain.Follow;
import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.repository.FollowRepository;
import haedal.Bootcamp2024_2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testExistsByFollowerAndFollowing() {
        User follower = new User("follower", "password", "Follower Name");
        User following = new User("following", "password", "Following Name");

        userRepository.save(follower);
        userRepository.save(following);

        Follow follow = new Follow(follower, following);
        followRepository.save(follow);

        boolean exists = followRepository.existsByFollowerAndFollowing(follower, following);
        assertTrue(exists);
    }
}