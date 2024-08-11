package io.online.videosite.repository;

import io.online.videosite.domain.JdbcJsonWebToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
public class JdbcJsonWebTokenRepositoryTests {
    @Autowired
    private JdbcJsonWebTokenRepository jsonWebTokenRepository;

    @Test
    public void testSelect() {
        try {
            System.err.println(this.jsonWebTokenRepository.getToken("c305b2d9-6af7-4ab3-aa19-29b0afe82299"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSave() {
        try {
            JdbcJsonWebToken jwt = new JdbcJsonWebToken();
            jwt.setJwtId(UUID.randomUUID().toString());
            jwt.setSubject("hello jwt");
            jwt.setIssuer("spring data jpa");
            jwt.setIssuedAt(new Date());
            jwt.setExpirationTime(new Date());
            jwt.setAudience("all");
            jwt.setToken(UUID.randomUUID().toString());
            this.jsonWebTokenRepository.createNewToken(jwt);
            System.err.println(jwt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        try {
            this.jsonWebTokenRepository.removeToken("c305b2d9-6af7-4ab3-aa19-29b0afe82299");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
