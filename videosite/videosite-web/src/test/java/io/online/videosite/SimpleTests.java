package io.online.videosite;

import io.online.videosite.constant.AuditStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.PathContainer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class SimpleTests {
    @Test
    public void testDataSize() {
        try {
            System.err.println(DataSize.parse("100", DataUnit.MEGABYTES));
            System.err.println(DataSize.parse("100MB"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegex() {
        try {
            String regex = "\\W";
            String text = "zww_1";
            Pattern p = Pattern.compile(regex);
            System.err.println(p.matcher(text).find());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileName() {
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

    @Test
    public void testPathPatternParser() {
        try {
            PathPatternParser parser = PathPatternParser.defaultInstance;
            PathPattern pattern = parser.parse("/videohub/**");
            System.err.println(pattern.matches(PathContainer.parsePath("/videohub/audit")));
            System.err.println(pattern.matches(PathContainer.parsePath("/videohub/audit/132131")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPasswordEncoder() {
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

    @Test
    public void testSubList() {
        try {
            List<Object> list = List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k");
            int limit = 10;
            System.err.println(list.size());
            System.err.println(list.subList(0, limit));
            System.err.println(list.subList(limit, list.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAuditStatus() {
        try {
            AuditStatus status = null;
            System.err.println(status == AuditStatus.PENDING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
