package io.online.videosite.api;

import io.online.videosite.domain.Category;
import io.online.videosite.domain.User;

import java.util.List;

/**
 * 视频类别服务接口
 *
 * @author 张维维
 */
public interface CategoryService {
    /**
     * 查询所有视频类别
     *
     * @return {@link List<Category>}
     */
    List<Category> query();

    /**
     * 检查类别名称是否存在
     *
     * @param category {@link List<Category>}
     * @return {@link boolean}
     */
    boolean checkExist(Category category);

    /**
     * 添加视频类别
     *
     * @param category {@link List<Category>}
     * @param user     {@link User}
     */
    void save(Category category, User user);
}
