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

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;

    public void followUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid follower ID"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid following ID"));

        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Follower and Following are the same");
        }

        Follow follow = new Follow(follower, following);
        followRepository.save(follow);
    }


    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid follower ID"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid following ID"));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new IllegalArgumentException("Follow relationship not found"));

        followRepository.delete(follow);
    }


    public Page<UserSimpleResponseDto> getFollowingUsers(Long id, Pageable pageable) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Page<Follow> followings = followRepository.findByFollower(user, pageable);
        return followings.map(follow -> new UserSimpleResponseDto(
                follow.getFollowing().getId(),
                follow.getFollowing().getUsername(),
                follow.getFollowing().getUserImage(),
                follow.getFollowing().getName()
        ));
    }


    public Page<UserSimpleResponseDto> getFollowerUsers(Long id, Pageable pageable) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Page<Follow> followers = followRepository.findByFollowing(user, pageable);
        return followers.map(follow -> new UserSimpleResponseDto(
                follow.getFollower().getId(),
                follow.getFollower().getUsername(),
                follow.getFollower().getUserImage(),
                follow.getFollower().getName()
        ));
    }
}