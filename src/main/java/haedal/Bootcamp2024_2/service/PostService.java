package haedal.Bootcamp2024_2.service;

import haedal.Bootcamp2024_2.domain.Post;
import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.response.PostResponseDto;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.repository.LikeRepository;
import haedal.Bootcamp2024_2.repository.PostRepository;
import haedal.Bootcamp2024_2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikeRepository likeRepository;


    public void savePost(Post post){
        Post saved = postRepository.save(post);
    }

    public List<PostResponseDto> getFollowingUsersPosts(Long userId) {
       User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Long> followingIds = user.getFollowings().stream()
                .map(follow -> follow.getFollowing().getId())
                .toList();

        List<Post> posts = postRepository.findByUser_IdIn(followingIds);
        return posts.stream().map(post -> convertPostToDto(post)).toList();
    }


    public List<PostResponseDto> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Post> posts = postRepository.findByUser(user);

        return posts.stream().map(post -> convertPostToDto(post)).toList();
    }

    private PostResponseDto convertPostToDto(Post post) {
        User user = post.getUser();
        UserSimpleResponseDto userSimpleResponseDto = new UserSimpleResponseDto(
                user.getId(),
                user.getUsername(),
                user.getImageUrl(),
                user.getName()
        );

        Long likeCount = likeRepository.countByPost(post);
        String createdAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));

        return new PostResponseDto(
                post.getId(),
                userSimpleResponseDto,
                post.getImageUrl(),
                post.getContext(),
                likeCount,
                likeRepository.existsByUserAndPost(user, post),
                createdAt
        );
    }
}