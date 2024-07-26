package haedal.Bootcamp2024_2.repository;

import haedal.Bootcamp2024_2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(Long userId);


//    @Query("SELECT u FROM User u JOIN FETCH u.followings WHERE u.userId = :userId")
//    Optional<User> findByIdWithFollowings(@Param("userId") Long userId);
}
