package haedal.Bootcamp2024_2.service;

import haedal.Bootcamp2024_2.domain.Post;
import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.response.PostResponseDto;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.repository.LikeRepository;
import haedal.Bootcamp2024_2.repository.PostRepository;
import haedal.Bootcamp2024_2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private UserService userService;


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
        posts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));

        return posts.stream().map(post -> convertPostToDto(user, post)).toList();
    }


    public List<PostResponseDto> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Post> posts = postRepository.findByUser(user);
        System.out.println(posts.toString());

        return posts.stream().map(post -> convertPostToDto(user, post)).toList();
    }


    private PostResponseDto convertPostToDto(User currentUser, Post post) {
        User author = post.getUser();
        UserSimpleResponseDto userSimpleResponseDto = userService.convertUserToSimpleDto(currentUser, author);

        return PostResponseDto.builder()
                .id(post.getId())
                .user(userSimpleResponseDto)
                .imageUrl(post.getImageUrl())
                .context(post.getContext())
                .likeCount(likeRepository.countByPost(post))
                .islike(likeRepository.existsByUserAndPost(currentUser, post))
                .createdAt(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")))
                .build();
    }
}