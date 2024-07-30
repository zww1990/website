package io.online.videosite;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

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

    public static void main(String[] args) throws Exception {
        JwtTests jwt = new JwtTests();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()//
                .subject("userlogin")//
                .issuer("zhangweiwei")//
                .issueTime(new Date())//
                .expirationTime(new Date(System.currentTimeMillis() + 2 * 60 * 1000))//
                .jwtID(UUID.randomUUID().toString())//
                .claim("username", "zhangweiwei")//
                .build();

        String token = jwt.createJwtToken(claimsSet);
        System.out.println("token = " + token);

        boolean verify = jwt.verify(token);
        System.out.println("verify = " + verify);

        JWTClaimsSet payload = jwt.getPayload(token);
        System.out.println("payload = " + payload);
    }
}
