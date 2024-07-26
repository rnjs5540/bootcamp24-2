package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.Post;
import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.request.PostRequestDto;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
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


    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User currentUser = (User)session.getAttribute("user");

        // 이미지 byte[]로 변경
        byte[] imageBytes = Base64.getDecoder().decode(postRequestDto.getImage());
        // 새로운 게시물 생성
        Post post = new Post(currentUser, imageBytes, postRequestDto.getContext());

        postService.savePost(post);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/following")
    public ResponseEntity<Page<Post>> getFollowingUsersPosts(HttpServletRequest request, Pageable pageable) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User currentUser = (User) session.getAttribute("user");

        Page<Post> posts = postService.getFollowingUsersPosts(currentUser, pageable);
        return ResponseEntity.ok(posts);
    }


    @PostMapping("/{postId}/like")
    // ResponseEntity<Void>? ResponseEntity<String>?
    public ResponseEntity<String> likePost(@PathVariable Long postId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User currentUser = (User)session.getAttribute("user");

        try {
            likeService.likePost(currentUser, postId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 이미 좋아요를 누른 경우 409 Conflict 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 게시물을 찾을 수 없는 경우 404 Not Found 반환
        }
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<String> unlikePost(HttpServletRequest request, @PathVariable Long postId) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User currentUser = (User) session.getAttribute("user");

        try {
            likeService.unlikePost(currentUser, postId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("좋아요가 존재하지 않습니다."); // 좋아요가 존재하지 않는 경우 404 Not Found 반환
        }
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<Page<UserSimpleResponseDto>> getUsersWhoLikedPost(@PathVariable Long postId, Pageable pageable) {
        Page<UserSimpleResponseDto> usersWhoLikedPost = likeService.getUsersWhoLikedPost(postId, pageable);
        return ResponseEntity.ok(usersWhoLikedPost);
    }
}