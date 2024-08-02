package haedal.Bootcamp2024_2.service;

import haedal.Bootcamp2024_2.domain.Follow;
import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.repository.FollowRepository;
import haedal.Bootcamp2024_2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public void followUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팔로워 회원입니다."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팔로잉 회원입니다."));

        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우 할 수 없습니다.");
        }
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalStateException("이미 팔로우중입니다.");
        }

        Follow follow = new Follow(follower, following);
        followRepository.save(follow);
    }


    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팔로워 회원입니다."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팔로잉 회원입니다."));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 존재하지 않습니다."));

        followRepository.delete(follow);
    }


    public List<UserSimpleResponseDto> getFollowingUsers(Long currentUserId, Long targetUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Follow> follows = followRepository.findByFollower(targetUser);
        return follows.stream()
                .map(follow -> userService.convertUserToSimpleDto(currentUser, follow.getFollowing()))
                .toList();
    }


    public List<UserSimpleResponseDto> getFollowerUsers(Long currentUserId, Long targetUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Follow> follows = followRepository.findByFollowing(targetUser);
        return follows.stream()
                .map(follow -> userService.convertUserToSimpleDto(currentUser, follow.getFollower()))
                .toList();
    }
}