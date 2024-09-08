package haedal.Bootcamp2024_2.service;

import haedal.Bootcamp2024_2.domain.Follow;
import haedal.Bootcamp2024_2.domain.User;
import haedal.Bootcamp2024_2.dto.response.UserSimpleResponseDto;
import haedal.Bootcamp2024_2.repository.FollowRepository;
import haedal.Bootcamp2024_2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired

    public FollowService(FollowRepository followRepository, UserRepository userRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void followUser(User currrentUser,Long targetUserId) {
        User targetUser =userRepository.findById(targetUserId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다"));

        if(currrentUser.equals(targetUser)) {
            throw new IllegalArgumentException("자기 자신은 팔로우 할 수 없습니다");
        }
        if(followRepository.existsByFollowerAndFollowing(currrentUser,targetUser)){
            throw new IllegalArgumentException("이미 팔로우 중입니다");
        }

        Follow follow = new Follow(currrentUser,targetUser);
        followRepository.save(follow);
    }


    public void unfollowUser(User currrentUser,Long targetUserId) {
        User targetUser =userRepository.findById(targetUserId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다"));

        Follow follow = followRepository.findByFollowerAndFollowing(currrentUser,targetUser)
                .orElseThrow(()->new IllegalArgumentException("팔로우 하고 있지 않습니다"));

        followRepository.delete(follow);
    }

    public List<UserSimpleResponseDto> getFollowingUsers(User currrentUser,Long targetUserId) {
        User targetUser =userRepository.findById(targetUserId)
                .orElseThrow(()->new IllegalArgumentException("존해하지 않는 회원입니다"));

        List<Follow> follows =followRepository.findByFollower(targetUser);
        return follows.stream()
                .map(follow -> userService.convertUserToSimpleDto(currrentUser,follow.getFollowing()))
                .toList();
    }


    public List<UserSimpleResponseDto> getFollowerUsers(User currentUser, Long targetUserId) {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Follow> follows = followRepository.findByFollowing(targetUser);
        return follows.stream()
                .map(follow -> userService.convertUserToSimpleDto(currentUser, follow.getFollower()))
                .toList();
    }


}