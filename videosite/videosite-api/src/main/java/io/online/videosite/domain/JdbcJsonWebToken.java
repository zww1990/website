package io.online.videosite.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * JSON令牌表
 * @author 张维维
 * @since 2024-08-11 12:53:16
 */
@Table(name = "t_json_web_token")
@Entity
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class JdbcJsonWebToken implements JsonWebToken {
    /**
     * 令牌的唯一标识符
     */
    @Id
    private String jwtId;
    /**
     * 令牌的主题，通常是用户的唯一标识符
     */
    private String subject;
    /**
     * 令牌的发行者
     */
    private String issuer;
    /**
     * 令牌的颁发时间，即创建时间
     */
    private Date issuedAt;
    /**
     * 令牌的过期时间
     */
    private Date expirationTime;
    /**
     * 令牌的生效时间，即创建时间
     */
    @Column(insertable = false, updatable = false)
    private Date notBefore;
    /**
     * 令牌的受众，表示该令牌针对哪些接收者
     */
    private String audience;
    /**
     * JSON令牌
     */
    private String token;
}
