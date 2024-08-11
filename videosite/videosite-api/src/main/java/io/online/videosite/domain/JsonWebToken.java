package io.online.videosite.domain;

import java.util.Date;

/**
 * JSON令牌
 * @author 张维维
 * @since 2024-08-11 12:53:16
 */
public interface JsonWebToken {
    /**
     * 令牌的唯一标识符
     */
    String getJwtId();
    /**
     * 令牌的主题，通常是用户的唯一标识符
     */
    String getSubject();
    /**
     * 令牌的发行者
     */
    String getIssuer();
    /**
     * 令牌的颁发时间，即创建时间
     */
    Date getIssuedAt();
    /**
     * 令牌的过期时间
     */
    Date getExpirationTime();
    /**
     * 令牌的生效时间，即创建时间
     */
    Date getNotBefore();
    /**
     * 令牌的受众，表示该令牌针对哪些接收者
     */
    String getAudience();
    /**
     * JSON令牌
     */
    String getToken();
}
