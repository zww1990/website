package io.online.videosite;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtTests {
    //使用uuid生成密钥
    private  final String secret= UUID.randomUUID().toString();
    //用户数据的key
    private  final String usernameKey="usernameKey";

    public  String createJwtToken(String username) throws Exception {
        //创建头部对象
        JWSHeader jwsHeader =
                new JWSHeader.Builder(JWSAlgorithm.HS256)       // 加密算法
                        .type(JOSEObjectType.JWT) // 静态常量
                        .build();

        //创建载荷
        Map<String,Object> map=new HashMap<String,Object>();
        map.put(usernameKey, username);
        Payload payload= new Payload(map);

        //创建签名器
        JWSSigner jwsSigner = new MACSigner(secret);//密钥

        //创建签名
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);// 头部+载荷
        jwsObject.sign(jwsSigner);//再+签名部分

        //生成token字符串
        return jwsObject.serialize();
    }

    public  boolean verify(String jwtStr) throws Exception {
        JWSObject jwsObject=JWSObject.parse(jwtStr);
        JWSVerifier jwsVerifier=new MACVerifier(secret);
        return jwsObject.verify(jwsVerifier);
    }

    public  String getUserNameFormJwt(String jwtStr) throws Exception{
        JWSObject jwsObject=JWSObject.parse(jwtStr);
        Map<String,Object> map=jwsObject.getPayload().toJSONObject();
        return (String) map.get(usernameKey);
    }

    public static void main(String[] args) throws Exception {
        System.err.println(new JwtTests().createJwtToken("hello world"));
    }
}
