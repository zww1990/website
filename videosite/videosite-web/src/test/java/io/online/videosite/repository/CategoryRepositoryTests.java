package io.online.videosite.repository;

import io.online.videosite.domain.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testSelect() {
        try {
            this.categoryRepository.findAll().forEach(System.err::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert() {
        try {
            LocalDateTime now = LocalDateTime.now();
            String name = "admin";
            Category entity = new Category();
            entity.setCategoryName("剧情");
            entity.setCreatedDate(now);
            entity.setModifiedDate(now);
            entity.setCreator(name);
            entity.setModifier(name);
            Category entity2 = new Category();
            entity2.setCategoryName("科幻");
            entity2.setCreatedDate(now);
            entity2.setModifiedDate(now);
            entity2.setCreator(name);
            entity2.setModifier(name);
            Category entity3 = new Category();
            entity3.setCategoryName("动作");
            entity3.setCreatedDate(now);
            entity3.setModifiedDate(now);
            entity3.setCreator(name);
            entity3.setModifier(name);
            this.categoryRepository.saveAll(List.of(entity, entity2, entity3)).forEach(System.err::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
