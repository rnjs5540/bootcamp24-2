package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.Post;
import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.request.PostRequestDto;
import haedal.Bootcamp2024_2.dto.response.PostResponseDto;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.service.AuthService;
import haedal.Bootcamp2024_2.service.ImageService;
import haedal.Bootcamp2024_2.service.LikeService;
import haedal.Bootcamp2024_2.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ImageService imageService;


    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request){
        User currentUser = authService.getCurrentUser(request);

        // 이미지 검증
        String imageUrl = postRequestDto.getImageUrl();
        if (!imageService.imageExists(imageUrl)) {
            throw new IllegalArgumentException("이미지가 존재하지 않거나 읽을 수 없습니다.");
        }

        // 새로운 게시물 생성
        Post post = new Post(currentUser, postRequestDto.getContent(), postRequestDto.getImageUrl());

        postService.savePost(post);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/following")
    public ResponseEntity<List<PostResponseDto>> getFollowingUsersPosts(HttpServletRequest request) {
        System.out.println(Arrays.toString(request.getCookies()));

        User currentUser = authService.getCurrentUser(request);

        List<PostResponseDto> posts = postService.getFollowingUsersPosts(currentUser);
        return ResponseEntity.ok(posts);
    }


    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        likeService.likePost(currentUser, postId);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<Void> unlikePost(HttpServletRequest request, @PathVariable Long postId) {
        User currentUser = authService.getCurrentUser(request);

        likeService.unlikePost(currentUser, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/{postId}/like")
    public ResponseEntity<List<UserSimpleResponseDto>> getUsersWhoLikedPost(@PathVariable Long postId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        List<UserSimpleResponseDto> usersWhoLikedPost = likeService.getUsersWhoLikedPost(currentUser, postId);
        return ResponseEntity.ok(usersWhoLikedPost);
    }
}