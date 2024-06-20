package io.online.videosite;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class HttpStatusCodeTests {
    @Test
    public void testHttpStatus() {
        try {
            HttpStatusCode code = HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value());
            System.err.println(code == HttpStatus.NOT_FOUND);
            System.err.println(code == HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
