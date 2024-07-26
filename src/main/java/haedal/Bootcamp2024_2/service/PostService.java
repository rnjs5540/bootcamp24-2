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
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikeRepository likeRepository;


    public void savePost(Post post) throws IOException {
        postRepository.save(post);
    }

    public Page<Post> getFollowingUsersPosts(User user, Pageable pageable) {
        User managedUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        List<Long> followingIds = user.getFollowings().stream()
                .map(follow -> follow.getFollowing().getUserId())
                .toList();

        return postRepository.findByUser_UserIdIn(followingIds, pageable);
    }


    public Page<PostResponseDto> getPostsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Page<Post> posts = postRepository.findByUser(user, pageable);

        return posts.map(this::convertPostToDto);
    }

    private PostResponseDto convertPostToDto(Post post) {
        UserSimpleResponseDto userSimpleResponseDto = new UserSimpleResponseDto(
                post.getUser().getUserId(),
                post.getUser().getUsername(),
                post.getUser().getUserImage()
        );
        Long likeCount = likeRepository.countByPost(post);

        return new PostResponseDto(
                post.getPostId(),
                userSimpleResponseDto,
                post.getImage(),
                post.getContext(),
                likeCount,
                post.getCreatedAt()
        );
    }
}