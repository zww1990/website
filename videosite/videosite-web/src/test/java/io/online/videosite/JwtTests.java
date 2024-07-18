package io.online.videosite;

import org.springframework.security.oauth2.jwt.Jwt;

public class JwtTests {
    public static void main(String[] args) {
        System.err.println(Jwt.withTokenValue("123456")
                .header("key", "value")
                .claim("key", "value")
                .build());
    }
}
