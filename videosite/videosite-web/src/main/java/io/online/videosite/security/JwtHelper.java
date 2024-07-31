package io.online.videosite.security;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.online.videosite.domain.User;
import io.online.videosite.properties.VideoSiteAppProperties;
import io.online.videosite.properties.VideoSiteAppProperties.JwtProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * JWT 辅助类
 * @author 张维维
 * @since 2024-07-31 13:14:09
 */
@Component
@Slf4j
@AllArgsConstructor
public class JwtHelper {
    private final VideoSiteAppProperties properties;

    /**
     * 创建JWT令牌
     * @param user 用户信息
     * @return JWT令牌，如果创建失败，返回null值
     */
    public String createToken(User user) {
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();
        JwtProperties config = this.properties.getJwt();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(config.getIssuer())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + config.getExpiration() * 1000L))
                .jwtID(UUID.randomUUID().toString().replace("-", ""))
                .build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            MACSigner macSigner = new MACSigner(config.getSecret());
            signedJWT.sign(macSigner);
            return signedJWT.serialize();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * 获取用户名
     * @param token JWT令牌
     * @return 用户名，如果解析失败，返回null值
     */
    public String getSubject(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * 校验JWT令牌
     * @param token JWT令牌
     * @return true or false
     */
    public boolean verifyToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            MACVerifier macVerifier = new MACVerifier(this.properties.getJwt().getSecret());
            return expiration.after(new Date()) && signedJWT.verify(macVerifier);
        } catch (Exception e) {
        	log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }
}
