package io.online.videosite.repository;

import io.online.videosite.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 权限数据访问对象接口
 *
 * @author 张维维
 * @since 2024-07-08 14:36:25
 */
public interface AuthorityRepository extends JpaRepository<Authority, Integer>,
        JpaSpecificationExecutor<Authority> {
    /**
     * Find By Authority
     * @param authority 权限编码
     * @return {@link Authority}
     */
    default Optional<Authority> findByAuthority(String authority) {
        return this.findOne((root, query, builder) ->
                builder.equal(root.get("authority"), authority));
    }
}
