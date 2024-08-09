package io.online.videosite.repository;

import io.online.videosite.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 视频类别数据访问对象接口
 *
 * @author 张维维
 */
public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    /**
     * Exists By Category Name
     * @param category {@link Category}
     * @return true or false
     */
    default boolean existsByCategoryName(Category category) {
        return this.exists((root, query, builder) ->
                builder.equal(root.get("categoryName"), category.getCategoryName()));
    }
}
