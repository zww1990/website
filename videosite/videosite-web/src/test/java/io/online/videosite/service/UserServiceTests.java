package io.online.videosite.service;

import io.online.videosite.api.UserService;
import io.online.videosite.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Test
    public void testQuery() {
        try {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("2");
            System.err.println(this.userService.query(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
