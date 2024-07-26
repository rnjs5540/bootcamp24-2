//package haedal.Bootcamp2024_2.service;
//
//import haedal.Bootcamp2024_2.domain.User;
//import haedal.Bootcamp2024_2.repository.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringBootTest
//@Transactional
//class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @AfterEach
//    public void afterEach() {
//        userRepository.deleteAll();
//    }
//
//    @Test
//    void 회원가입() {
//        // given
//        User user = new User();
//        user.setUsername("this is username");
//        user.setName("justName");
//
//        // when
//        Long saveId = userService.join(user);
//
//        // then
//        User findUser = userService.findByUserId(saveId).get();
//        assertThat(user.getName()).isEqualTo(findUser.getName());
//    }
//
//    @Test
//    public void 중복_회원_예외() {
//        // given
//        User user1 = new User();
//        user1.setName("name");
//        user1.setUsername("username1");
//
//        User user2 = new User();
//        user2.setName("spring");
//        user2.setUsername("username1");
//
//        // when
//        userService.join(user1);
//        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.join(user2));
//
//        // then
//        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//    }
//
////    @Test
////    void findMembers() {
////        회원가입();
////        중복_회원_예외();
////        List<User> userList = userService.findMembers();
////        for (User user : userList) {
////            System.out.println(user.getUsername());
////            System.out.println(user.getName());
////        }
////    }
//
//    @Test
//    void findOne() {
//        // Implement test logic for finding one member
//    }
//}