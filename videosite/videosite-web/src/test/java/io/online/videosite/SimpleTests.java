package io.online.videosite;

import io.online.videosite.constant.AuditStatus;
import io.online.videosite.domain.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

public class SimpleTests {

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

    @Test
    public void testUser() {
        try {
            User user = new User();
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            System.err.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
