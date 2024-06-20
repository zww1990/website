package io.online.videosite;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.nio.file.Paths;

public class StringUtilsTests {
    @Test
    public void testGetFilename() {
        try {
            String path = "微信头像.jpg";
            System.err.println(StringUtils.getFilename(path));
            System.err.println(StringUtils.getFilenameExtension(path));
            System.err.println(StringUtils.stripFilenameExtension(path));
            System.err.println(Paths.get("/a/b", path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
