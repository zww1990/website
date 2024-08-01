package io.online.videosite;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

public class JwtTests {
    //使用uuid生成密钥
    private final String secret = "UUIDrandomUUIDtoString1234567890";

    /**
     * 创建jwt令牌
     */
    public String createJwtToken(JWTClaimsSet claimsSet) throws Exception {
        //创建头部对象
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256)//
                        .type(JOSEObjectType.JWT)//
                        .build();

        //创建签名器
        JWSSigner jwsSigner = new MACSigner(secret);

        //创建签名
        SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
        signedJWT.sign(jwsSigner);

        //生成token字符串
        return signedJWT.serialize();
    }

    /**
     * 验证jwt令牌
     */
    public boolean verify(String jwtStr) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(jwtStr);
        JWSVerifier jwsVerifier = new MACVerifier(secret);
        return signedJWT.verify(jwsVerifier);
    }

    /**
     * 获取jwt载体
     */
    public JWTClaimsSet getPayload(String jwtStr) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(jwtStr);
        return signedJWT.getJWTClaimsSet();
    }

    @Test
    public void testMain() throws Exception {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()//
                .subject("userlogin")//
                .issuer("zhangweiwei")//
                .issueTime(new Date())//
                .expirationTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000))//
                .jwtID(UUID.randomUUID().toString().replace("-", ""))//
                .claim("username", "zhangweiwei")//
                .build();

        String token = this.createJwtToken(claimsSet);
        System.out.println("token = " + token);

        boolean verify = this.verify(token);
        System.out.println("verify = " + verify);

        JWTClaimsSet payload = this.getPayload(token);
        System.out.println("payload = " + payload);
    }

    @Test
    public void testValidatingJwtAccessTokens() throws Exception {
        String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybG9naW4iLCJpc3MiOiJ6aGFuZ3dlaXdlaSIsImV4cCI6MTcyMjQ5MjY3MywiaWF0IjoxNzIyNDkyMzczLCJqdGkiOiIyOGQ0YTYxZWI1YWE0ODVmOWI1NTdjNWUzZmIxMTZmZSIsInVzZXJuYW1lIjoiemhhbmd3ZWl3ZWkifQ.2JVtXWkdP_Cohkc8l9CSYVlrjTFH025X6Ov77qlXmL0";
        System.err.println(accessToken);

        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

        JWKSource<SecurityContext> keySource = new ImmutableSecret<>(secret.getBytes());

        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(
                JWSAlgorithm.HS256,
                keySource);
        jwtProcessor.setJWSKeySelector(keySelector);

        SecurityContext ctx = new SimpleSecurityContext();
        JWTClaimsSet claimsSet = jwtProcessor.process(accessToken, ctx);
        System.out.println(claimsSet.toJSONObject());
    }
}
