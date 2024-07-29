package haedal.Bootcamp2024_2.repository;

import haedal.Bootcamp2024_2.domain.Post;
import haedal.Bootcamp2024_2.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Long countByUser(User user);
    Page<Post> findByUser(User user, Pageable pageable);

    Page<Post> findByUser_IdIn(List<Long> userIds, Pageable pageable);
//      => Post 엔티티에서 User와 관계를 맺고 있는 필드 user를 참조하고, 그 User 엔티티 내의 userId 필드를 사용하여 쿼리를 작성
//    Page<Post> findByUserIdIn(List<Long> userIds, Pageable pageable);
//      => Post 엔티티에서 userId라는 필드를 찾으려고 시도 -> 에러
}