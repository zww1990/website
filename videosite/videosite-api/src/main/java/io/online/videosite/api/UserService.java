package io.online.videosite.api;

import io.online.videosite.domain.User;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author 张维维
 */
public interface UserService {
    /**
     * 按用户名查询用户信息
     *
     * @param user {@link User}
     * @return {@link User}
     */
    User query(User user);

    /**
     * 添加新用户
     *
     * @param user {@link User}
     */
    void save(User user);

    /**
     * 查询所有用户
     *
     * @return {@link List<User>}
     */
    List<User> queryUsers();
}
