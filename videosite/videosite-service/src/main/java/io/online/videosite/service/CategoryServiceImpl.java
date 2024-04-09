package io.online.videosite.service;

import io.online.videosite.api.CategoryService;
import io.online.videosite.domain.Category;
import io.online.videosite.domain.User;
import io.online.videosite.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 视频类别服务接口实现类
 *
 * @author 张维维
 */
@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> query() {
        return this.categoryRepository.findAll();
    }

    @Override
    public boolean checkExist(Category category) {
        log.info("checkExist(): category = {}", category);
        return this.categoryRepository.exists((root, query, builder) ->
                builder.equal(root.get("categoryName"), category.getCategoryName()));
    }

    @Override
    public void save(Category category, User user) {
        log.info("save(): category = {}, user = {}", category, user);
        category.setCreatedDate(LocalDateTime.now());
        category.setModifiedDate(category.getCreatedDate());
        category.setCreator(user.getUsername());
        category.setModifier(user.getUsername());
        this.categoryRepository.save(category);
    }
}
