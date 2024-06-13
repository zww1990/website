package io.online.videosite.service;

import io.online.videosite.api.VideoService;
import io.online.videosite.constant.AuditStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VideoServiceTests {
    @Autowired
    private VideoService videoService;

    @Test
    public void testQuery() {
        try {
            Integer categoryId = 1;
            this.videoService.query(categoryId).forEach(System.err::println);
            this.videoService.query(categoryId, AuditStatus.PENDING).forEach(System.err::println);
            this.videoService.query(categoryId, AuditStatus.PENDING, AuditStatus.PASSED).forEach(System.err::println);
            this.videoService.query(categoryId, AuditStatus.PENDING, AuditStatus.PASSED, AuditStatus.UNPASSED).forEach(System.err::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryForKeyword() {
        try {
            this.videoService.queryForKeyword("中方").forEach(System.err::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
