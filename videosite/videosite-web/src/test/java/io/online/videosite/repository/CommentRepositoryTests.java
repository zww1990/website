package io.online.videosite.repository;

import io.online.videosite.domain.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class CommentRepositoryTests {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void testSelect() {
        try {
            this.commentRepository.findAll().forEach(System.err::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() {
        try {
            LocalDateTime now = LocalDateTime.now();
            String name = "test";
            Comment entity = new Comment();
            entity.setContent("泰裤辣" + now);
            entity.setVideoId(1);
            entity.setCreator(name);
            entity.setModifier(name);
            this.commentRepository.saveAll(List.of(entity)).forEach(System.err::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
