package io.online.videosite.repository;

import io.online.videosite.domain.JdbcJsonWebToken;
import io.online.videosite.domain.JsonWebToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.function.Function;

/**
 * JDBC JSON令牌表数据访问对象接口
 * @author 张维维
 * @since 2024-08-11 12:59:34
 */
public interface JdbcJsonWebTokenRepository extends JpaRepository<JdbcJsonWebToken, String>,
        JpaSpecificationExecutor<JdbcJsonWebToken>, JsonWebTokenRepository {
    @Override
    default void createNewToken(JsonWebToken token) {
        this.save((JdbcJsonWebToken) token);
    }

    @Override
    default Optional<JsonWebToken> getToken(String jwtId) {
        return this.findById(jwtId).map(Function.identity());
    }

    @Override
    default void removeToken(String jwtId) {
        this.deleteById(jwtId);
    }
}
