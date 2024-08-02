package haedal.Bootcamp2024_2.controller;

import haedal.Bootcamp2024_2.domain.Post;
import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.request.PostRequestDto;
import haedal.Bootcamp2024_2.dto.response.PostResponseDto;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.service.AuthService;
import haedal.Bootcamp2024_2.service.LikeService;
import haedal.Bootcamp2024_2.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private LikeService likeService;
    @Autowired
    AuthService authService;


    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {
        User currentUser = authService.getCurrentUser(request);

//        // 이미지 검증
//        String imageUrl = postRequestDto.getImageUrl();
//        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/userImages";
//        Path imagePath = Paths.get(uploadDir).resolve(imageUrl).normalize();
//        if (!Files.exists(imagePath) || !Files.isReadable(imagePath)) {
//            throw new IllegalArgumentException("이미지가 존재하지 않거나 읽을 수 없습니다.");
//        }

        // 새로운 게시물 생성
        Post post = new Post(currentUser, postRequestDto.getContext(), postRequestDto.getImageUrl());

        postService.savePost(post);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/following")
    public ResponseEntity<List<PostResponseDto>> getFollowingUsersPosts(HttpServletRequest request) {
        System.out.println(Arrays.toString(request.getCookies()));

        User currentUser = authService.getCurrentUser(request);

        List<PostResponseDto> posts = postService.getFollowingUsersPosts(currentUser.getId());
        return ResponseEntity.ok(posts);
    }


    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);

        likeService.likePost(currentUser.getId(), postId);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<String> unlikePost(HttpServletRequest request, @PathVariable Long postId) {
        User currentUser = authService.getCurrentUser(request);

        likeService.unlikePost(currentUser.getId(), postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/{postId}/like")
    public ResponseEntity<List<UserSimpleResponseDto>> getUsersWhoLikedPost(@PathVariable Long postId) {
        List<UserSimpleResponseDto> usersWhoLikedPost = likeService.getUsersWhoLikedPost(postId);
        return ResponseEntity.ok(usersWhoLikedPost);
    }
}