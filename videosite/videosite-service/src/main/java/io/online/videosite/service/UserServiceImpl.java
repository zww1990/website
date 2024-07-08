package io.online.videosite.service;

import io.online.videosite.api.UserService;
import io.online.videosite.constant.UserType;
import io.online.videosite.domain.User;
import io.online.videosite.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务接口实现类
 *
 * @author 张维维
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User query(User user) {
        return this.userRepository.findByUsername(user.getUsername()).orElse(null);
    }

    @Override
    public void save(User user) {
        user.setCreator(user.getUsername());
        user.setModifier(user.getUsername());
        user.setUserType(UserType.NORMAL);
        this.userRepository.save(user);
    }

    @Override
    public List<User> queryUsers() {
        return this.userRepository.findAll();
    }
}
