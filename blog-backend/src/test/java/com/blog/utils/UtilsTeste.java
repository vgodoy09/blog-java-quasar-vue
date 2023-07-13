package com.blog.utils;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.blog.config.SecurityConstants;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class UtilsTeste {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // String encode = bCryptPasswordEncoder.encode("WEylogP21R6I5LoyrLv1");
        String encode = bCryptPasswordEncoder.encode("12345");
        System.out.println("######################################## " + encode);
        System.out.println(bCryptPasswordEncoder.matches("1233456789",
                "$2a$10$RFrAKaEIvv/VgqqOWPSB2.5dATamLgSsDWrPUrXD2Ac64jEvUDfLC"));

        String token = JWT.create().withSubject("victor.godoy@ymvig.com.br")
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(HMAC512(SecurityConstants.SECRET.getBytes()));

        System.out.println("######################################## " + token);

        String user = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build().verify(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2cC5nb2RveUBvdXRsb29rLmNvbSIsImV4cCI6MTYyNjc1Mzg3OH0.QY456WBCsUfxuJbmJ28v2x5E9ZIsoi6MR1AuxtZ-8DQU7JJpT_Rfr7Gtq8pzGg5aMWBYmhkjxjDOBOxswdqd4A")
                .getSubject();

        // String user =
        // JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build().verify(encode)
        // .getSubject();
        System.out.println("######################################## " + user);
        // System.out.println("######################################## " + user);
    }
}
