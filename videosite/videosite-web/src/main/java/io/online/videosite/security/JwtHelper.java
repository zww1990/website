package io.online.videosite.security;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.online.videosite.constant.Constants;
import io.online.videosite.domain.User;
import io.online.videosite.properties.VideoSiteAppProperties;
import io.online.videosite.properties.VideoSiteAppProperties.JwtProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * JWT 辅助类
 * @author 张维维
 * @since 2024-07-31 13:14:09
 */
@Component
@AllArgsConstructor
public class JwtHelper {
    private final VideoSiteAppProperties properties;

    /**
     * 创建JWT令牌
     * @param user 用户信息
     * @return JWT令牌
     */
    public String createJwtToken(User user) {
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT)
                .build();
        JwtProperties config = this.properties.getJwt();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(config.getSubject())
                .issuer(config.getIssuer())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + config.getExpiration() * 1000L))
                .jwtID(UUID.randomUUID().toString().replace("-", ""))
                .claim(Constants.JWT_USER_KEY, user.getId())
                .build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            MACSigner macSigner = new MACSigner(config.getSecret());
            signedJWT.sign(macSigner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return signedJWT.serialize();
    }
}
