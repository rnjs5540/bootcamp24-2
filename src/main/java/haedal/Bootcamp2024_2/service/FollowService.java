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

    public void followUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팔로워 회원입니다."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팔로잉 회원입니다."));

        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("팔로워 회원과 팔로잉 회원이 같습니다.");
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


    public List<UserSimpleResponseDto> getFollowingUsers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Follow> followings = followRepository.findByFollower(user);
        return followings.stream().map(follow -> new UserSimpleResponseDto(
                follow.getFollowing().getId(),
                follow.getFollowing().getUsername(),
                follow.getFollowing().getImageUrl(),
                follow.getFollowing().getName()
        )).toList();
    }


    public List<UserSimpleResponseDto> getFollowerUsers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Follow> followers = followRepository.findByFollowing(user);
        return followers.stream().map(follow -> new UserSimpleResponseDto(
                follow.getFollowing().getId(),
                follow.getFollowing().getUsername(),
                follow.getFollowing().getImageUrl(),
                follow.getFollowing().getName()
        )).toList();
    }
}