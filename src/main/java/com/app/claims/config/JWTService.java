package com.app.claims.config;


import com.app.claims.entity.User;
import com.app.claims.exception.InvalidToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expityTime}")
    private int expityTime;

    private Algorithm algorithm;

    @PostConstruct
    private void postConstruct(){
        algorithm = Algorithm.HMAC256(secretKey);
    }

    public String generateToken(User user){
        return JWT.create().
                withClaim("username", user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis()+expityTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUsername(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
            return decodedJWT.getClaim("username").asString();
        } catch (TokenExpiredException e){
            throw new InvalidToken(e.getMessage());
        }

    }

}
