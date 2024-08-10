package io.online.videosite.service;

import io.online.videosite.api.UserService;
import io.online.videosite.constant.UserType;
import io.online.videosite.domain.Authority;
import io.online.videosite.domain.User;
import io.online.videosite.repository.AuthorityRepository;
import io.online.videosite.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 用户服务接口实现类
 *
 * @author 张维维
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public User query(User user) {
        return this.userRepository.findByUsername(user.getUsername()).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        user.setCreator(user.getUsername());
        user.setModifier(user.getUsername());
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        Authority authority = this.authorityRepository.findByAuthority(UserType.ROLE_NORMAL.name())
                .orElseGet(() -> {
                    Authority a = new Authority();
                    a.setCreator(user.getUsername());
                    a.setModifier(user.getUsername());
                    a.setAuthority(UserType.ROLE_NORMAL.name());
                    a.setAuthName(UserType.ROLE_NORMAL.getUserTypeName());
                    return a;
                });
        user.setAuthorities(Set.of(authority));
        this.userRepository.save(user);
    }

    @Override
    public List<User> queryUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("此用户名[%s]不存在！", username)));
    }
}
