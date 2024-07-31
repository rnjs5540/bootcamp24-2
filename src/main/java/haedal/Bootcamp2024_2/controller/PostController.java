package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.Post;
import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.request.PostRequestDto;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.service.AuthService;
import haedal.Bootcamp2024_2.service.LikeService;
import haedal.Bootcamp2024_2.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private LikeService likeService;
    @Autowired
    AuthService authService;


    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {
        // 이미지 보내고, 포스트아이디 리턴하면 내용 보내기
        User currentUser = authService.getCurrentUser(request);

        // 이미지 byte[]로 변경
        byte[] imageBytes = Base64.getDecoder().decode(postRequestDto.getImage());
        // 새로운 게시물 생성
        Post post = new Post(currentUser, imageBytes, postRequestDto.getContext());

        postService.savePost(post);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/following")
    public ResponseEntity<Page<Post>> getFollowingUsersPosts(HttpServletRequest request, Pageable pageable) {
        User currentUser = authService.getCurrentUser(request);

        Page<Post> posts = postService.getFollowingUsersPosts(currentUser.getId(), pageable);
        return ResponseEntity.ok(posts);
    }


    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        likeService.likePost(currentUser.getId(), postId);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<String> unlikePost(HttpServletRequest request, @PathVariable Long postId) {
        User currentUser = authService.getCurrentUser(request);

        likeService.unlikePost(currentUser.getId(), postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<Page<UserSimpleResponseDto>> getUsersWhoLikedPost(@PathVariable Long postId, Pageable pageable) {
        Page<UserSimpleResponseDto> usersWhoLikedPost = likeService.getUsersWhoLikedPost(postId, pageable);
        return ResponseEntity.ok(usersWhoLikedPost);
    }
}