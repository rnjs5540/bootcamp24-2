package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.Post;
import haedal.Bootcamp2024_2.domain.User;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<Void> createPost(@RequestParam("image") MultipartFile image, @RequestParam("content") String content,
                                           HttpServletRequest request) throws IOException {
        User currentUser = authService.getCurrentUser(request);

        String imageUrl = imageService.savePostImage(image);

        Post post = new Post(currentUser, content, imageUrl);

        postService.savePost(post);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUser(@PathVariable Long userId) {
        List<PostResponseDto> posts = postService.getPostsByUser(userId);
        return ResponseEntity.ok(posts);
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