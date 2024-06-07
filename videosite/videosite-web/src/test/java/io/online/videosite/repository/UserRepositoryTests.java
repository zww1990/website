package io.online.videosite.repository;

import io.online.videosite.constant.UserType;
import io.online.videosite.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSelect() {
        try {
            this.userRepository.findAll().forEach(System.err::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() {
        try {
            LocalDateTime now = LocalDateTime.now();
            User entity = new User();
            entity.setNickname("杰伦");
            entity.setPassword(this.passwordEncoder.encode("123456"));
            entity.setUsername("admin");
            entity.setUserType(UserType.ADMIN);
            entity.setCreatedDate(now);
            entity.setModifiedDate(now);
            entity.setCreator(entity.getUsername());
            entity.setModifier(entity.getUsername());
            User entity2 = new User();
            entity2.setNickname("心凌");
            entity2.setPassword(this.passwordEncoder.encode("654321"));
            entity2.setUsername("test");
            entity2.setUserType(UserType.NORMAL);
            entity2.setCreatedDate(now);
            entity2.setModifiedDate(now);
            entity2.setCreator(entity.getUsername());
            entity2.setModifier(entity.getUsername());
            this.userRepository.saveAll(List.of(entity, entity2)).forEach(System.err::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
