package io.online.videosite;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

public class JwtTests {

    private RSAKey rsaKey;
    private String token;
    private RSAKey publicJWK;

    @BeforeEach
    public void createKey() {
        try {
            rsaKey = new RSAKeyGenerator(2048).generate();
            publicJWK = rsaKey.toPublicJWK();
            System.err.println("rsaKey=" + rsaKey);
            System.err.println("publicJWK=" + publicJWK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createToken() {
        try {
            RSASSASigner signer = new RSASSASigner(rsaKey);
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()//
                    .subject("userlogin")//
                    .issuer("张维维")//
                    .issueTime(new Date())//
                    .expirationTime(new Date(System.currentTimeMillis() + 1 * 60 * 1000))//
                    .jwtID(UUID.randomUUID().toString())//
                    .claim("username", "zhangweiwei")//
                    .build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
            signedJWT.sign(signer);
            token = signedJWT.serialize();
            System.err.println("token=" + token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void checkToken() {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            RSASSAVerifier verifier = new RSASSAVerifier(publicJWK);
            signedJWT.verify(verifier);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            System.err.println(claimsSet.getStringClaim("username"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
