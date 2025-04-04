package io.online.videosite;

import io.online.videosite.domain.User;
import io.online.videosite.properties.VideoSiteAppProperties;
import io.online.videosite.repository.JsonWebTokenRepository;
import io.online.videosite.repository.impl.DefaultJsonWebTokenRepository;
import io.online.videosite.security.JwtHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class JwtHelperTests {
    private JwtHelper helper = null;

    @BeforeEach
    public void init() {
        VideoSiteAppProperties properties = new VideoSiteAppProperties();
        JsonWebTokenRepository jsonWebTokenRepository = new DefaultJsonWebTokenRepository();
        // 1min
        properties.getJwt().setExpiration(Duration.ofMinutes(1));
        helper = new JwtHelper(properties, jsonWebTokenRepository);
    }

    @Test
    public void testCreateToken() {
        try {
            User user = new User();
            user.setUsername("spring");
            String token = helper.createToken(user);
            System.err.println(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVerifyToken() {
        try {
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTcHJpbmdTZWN1cml0eSIsInN1YiI6InNwcmluZyIsImV4cCI6MTcyMjQ4NTQxOSwiaWF0IjoxNzIyNDg1MzU5LCJqdGkiOiJlZjcyOTZhNzNkYjg0N2I0OGVmOTJkMzcyMGE0ODliMCJ9.999IZ0i0O69tn7-snVd7L2Na6dXGn1xmuhdDfxmYGkw";
            boolean result = helper.verifyToken(token, null);
            System.err.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
