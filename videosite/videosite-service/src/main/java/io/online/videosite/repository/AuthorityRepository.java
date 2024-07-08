package io.online.videosite.repository;

import io.online.videosite.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 权限数据访问对象接口
 *
 * @author 张维维
 * @since 2024-07-08 14:36:25
 */
public interface AuthorityRepository extends JpaRepository<Authority, Integer>,
        JpaSpecificationExecutor<Authority> {
}
