package io.online.videosite.repository;

import io.online.videosite.domain.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * JSON令牌存储库
 * @author 张维维
 * @since 2024-08-11 12:59:34
 */
public interface JsonWebTokenRepository {
    Logger log = LoggerFactory.getLogger(JsonWebTokenRepository.class);

    /**
     * Create New Token
     * @param token {@link JsonWebToken}
     */
    default void createNewToken(JsonWebToken token) {
        log.info("default:createNewToken(): token = {}", token);
    }
    /**
     * Get Token
     * @param jwtId 令牌的唯一标识符
     * @return {@link JsonWebToken}
     */
    default Optional<JsonWebToken> getToken(String jwtId) {
        log.info("default:getToken(): jwtId = {}", jwtId);
        return Optional.empty();
    }
    /**
     * Remove Token
     * @param jwtId 令牌的唯一标识符
     */
    default void removeToken(String jwtId) {
        log.info("default:removeToken(): jwtId = {}", jwtId);
    }
}
