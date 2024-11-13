package io.online.videosite.security;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import io.online.videosite.domain.JdbcJsonWebToken;
import io.online.videosite.domain.JsonWebToken;
import io.online.videosite.domain.User;
import io.online.videosite.properties.VideoSiteAppProperties;
import io.online.videosite.properties.VideoSiteAppProperties.JwtProperties;
import io.online.videosite.repository.JsonWebTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
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
    private final JsonWebTokenRepository jsonWebTokenRepository;

    /**
     * 创建JWT令牌
     * @param user {@link User}
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
                .expirationTime(new Date(System.currentTimeMillis() + config.getExpiration().toMillis()))
                .jwtID(UUID.randomUUID().toString().replace("-", ""))
                .build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            MACSigner macSigner = new MACSigner(config.getSecret());
            signedJWT.sign(macSigner);
            String token = signedJWT.serialize();
            // 持久化jwt
            this.jsonWebTokenRepository.createNewToken(new JdbcJsonWebToken()
                    .setToken(token)
                    .setJwtId(jwtClaimsSet.getJWTID())
                    .setSubject(jwtClaimsSet.getSubject())
                    .setIssuer(jwtClaimsSet.getIssuer())
                    .setIssuedAt(jwtClaimsSet.getIssueTime())
                    .setExpirationTime(jwtClaimsSet.getExpirationTime()));
            return token;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * 获取JSON令牌
     * @param token JWT令牌
     * @return {@link JsonWebToken}
     */
    public JsonWebToken getJsonWebToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            return new JdbcJsonWebToken()
                    .setToken(token)
                    .setJwtId(jwtClaimsSet.getJWTID())
                    .setSubject(jwtClaimsSet.getSubject())
                    .setIssuer(jwtClaimsSet.getIssuer())
                    .setIssuedAt(jwtClaimsSet.getIssueTime())
                    .setExpirationTime(jwtClaimsSet.getExpirationTime());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * 校验JWT令牌
     * @param token JWT令牌
     * @param jwtId JWT ID
     * @return true or false
     */
    public boolean verifyToken(String token, String jwtId) {
        try {
            DefaultJWTProcessor<SecurityContext> processor = new DefaultJWTProcessor<>();
            ImmutableSecret<SecurityContext> secret = new ImmutableSecret<>(this.properties.getJwt().getSecret().getBytes());
            JWSVerificationKeySelector<SecurityContext> selector = new JWSVerificationKeySelector<>(JWSAlgorithm.HS256, secret);
            SimpleSecurityContext context = new SimpleSecurityContext();
            processor.setJWSKeySelector(selector);
            DefaultJWTClaimsVerifier<SecurityContext> verifier = new DefaultJWTClaimsVerifier<>(null, Set.of(
                    JWTClaimNames.SUBJECT,
                    JWTClaimNames.ISSUED_AT,
                    JWTClaimNames.ISSUER,
                    JWTClaimNames.EXPIRATION_TIME,
                    JWTClaimNames.JWT_ID));
            processor.setJWTClaimsSetVerifier(verifier);
            processor.process(token, context);
            return true;
        } catch (Exception e) {
        	log.error(e.getLocalizedMessage(), e);
//            if (jwtId != null) {
//                // 验证失败后，将数据库里存在的删除掉。
//                this.jsonWebTokenRepository.removeToken(jwtId);
//            }
            return false;
        }
    }
}
