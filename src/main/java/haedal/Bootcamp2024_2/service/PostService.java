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
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final ImageService imageService;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository, UserService userService, ImageService imageService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.imageService = imageService;
    }


    public void savePost(Post post){
        Post saved = postRepository.save(post);
    }

    public List<PostResponseDto> getFollowingUsersPosts(User currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<User> followingUsers = user.getFollowings().stream()
                .map(follow -> follow.getFollowing())
                .toList();

        List<Post> posts = postRepository.findByUserIn(followingUsers);
        posts.sort((p1, p2) ->
                p2.getCreatedAt().compareTo(p1.getCreatedAt()));

        return posts.stream().map(post -> convertPostToDto(user, post)).toList();
    }


    public List<PostResponseDto> getPostsByUser(Long targetUserId) {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Post> posts = postRepository.findByUser(targetUser);
        posts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));

        return posts.stream().map(post -> convertPostToDto(targetUser, post)).toList();
    }


    private PostResponseDto convertPostToDto(User currentUser, Post post) {
        User author = post.getUser();
        UserSimpleResponseDto userSimpleResponseDto = userService.convertUserToSimpleDto(currentUser, author);
        String imageUrl = post.getImageUrl();
        String imageData = imageService.encodeImageToBase64(System.getProperty("user.dir") + "/src/main/resources/static/" + imageUrl);

        return PostResponseDto.builder()
                .id(post.getId())
                .user(userSimpleResponseDto)
                .imageData(imageData)
                .content(post.getContent())
                .likeCount(likeRepository.countByPost(post))
                .isLike(likeRepository.existsByUserAndPost(currentUser, post))
                .createdAt(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")))
                .build();
    }
}