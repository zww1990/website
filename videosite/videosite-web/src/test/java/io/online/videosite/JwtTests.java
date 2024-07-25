package io.online.videosite;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import java.util.HashMap;
import java.util.Map;

public class JwtTests {
    //使用uuid生成密钥
    private final String secret = "UUIDrandomUUIDtoString1234567890";

    /**
     * 创建jwt令牌
     */
    public String createJwtToken(Map<String, Object> jsonObject) throws Exception {
        //创建头部对象
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256) // 加密算法
                        .type(JOSEObjectType.JWT) // 静态常量
                        .build();

        //创建载荷
        Payload payload = new Payload(jsonObject);

        //创建签名器
        JWSSigner jwsSigner = new MACSigner(secret);//密钥

        //创建签名
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);// 头部+载荷
        jwsObject.sign(jwsSigner);//再+签名部分

        //生成token字符串
        return jwsObject.serialize();
    }

    /**
     * 验证jwt令牌
     */
    public boolean verify(String jwtStr) throws Exception {
        JWSObject jwsObject = JWSObject.parse(jwtStr);
        JWSVerifier jwsVerifier = new MACVerifier(secret);
        return jwsObject.verify(jwsVerifier);
    }

    /**
     * 获取jwt载体
     */
    public Map<String, Object> getPayload(String jwtStr) throws Exception {
        JWSObject jwsObject = JWSObject.parse(jwtStr);
        return jwsObject.getPayload().toJSONObject();
    }

    public static void main(String[] args) throws Exception {
        JwtTests jwt = new JwtTests();

        Map<String, Object> map = new HashMap<>();
        map.put("text", "hello world");

        String token = jwt.createJwtToken(map);
        System.out.println("token = " + token);

        boolean verify = jwt.verify(token);
        System.out.println("verify = " + verify);

        Map<String, Object> payload = jwt.getPayload(token);
        System.out.println("payload = " + payload);
    }
}
