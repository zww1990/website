package io.online.videosite;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTests {
    @Test
    public void testEncode() {
        try {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String raw = "123456";
            System.err.println("原始密码：" + raw);
            String encode = encoder.encode(raw);
            System.err.println("加密后密码：" + encode);
            System.err.println("是否匹配：" + encoder.matches(raw, encode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
