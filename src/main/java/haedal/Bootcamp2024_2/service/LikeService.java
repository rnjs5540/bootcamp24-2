package haedal.Bootcamp2024_2.service;

import haedal.Bootcamp2024_2.domain.Like;
import haedal.Bootcamp2024_2.domain.Post;
import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.repository.LikeRepository;
import haedal.Bootcamp2024_2.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;

    public void likePost(User currentUser, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        if (likeRepository.existsByUserAndPost(currentUser, post)) {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
        }

        Like like = new Like(currentUser, post);
        likeRepository.save(like);
    }

    public void unlikePost(User currentUser, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        Like like = likeRepository.findByUserAndPost(currentUser, post)
                .orElseThrow(() -> new IllegalArgumentException("좋아요가 존재하지 않습니다."));
        likeRepository.delete(like);
    }


    public List<UserSimpleResponseDto> getUsersWhoLikedPost(User currentUser, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));

        List<Like> likes = likeRepository.findByPost(post);
        return likes.stream()
                .map(like -> userService.convertUserToSimpleDto(currentUser, like.getUser()))
                .toList();
    }
}