package io.online.videosite.repository;

import io.online.videosite.constant.AuditStatus;
import io.online.videosite.domain.Video;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class VideoRepositoryTests {
    @Autowired
    private VideoRepository videoRepository;

    @Test
    public void testSelect() {
        try {
            this.videoRepository.findAll().forEach(System.err::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() {
        try {
            Video entity = new Video();
            entity.setAuditStatus(AuditStatus.PENDING);
            entity.setCategoryId(1);
            entity.setVideoHits(0);
            entity.setVideoLink("/video/001.mp4");
            entity.setVideoLogo("/image/001.jpg");
            entity.setVideoName("最伟大的作品");
            entity.setCreatedDate(LocalDateTime.now());
            entity.setCreator("admin");
            entity.setModifiedDate(entity.getCreatedDate());
            entity.setModifier(entity.getCreator());
            this.videoRepository.saveAll(List.of(entity)).forEach(System.err::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
