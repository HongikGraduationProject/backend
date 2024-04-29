package com.hongik.graduationproject.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class TokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecretKey;
    @Value("${jwt.access-token-time}")
    private long accessTokenTime;
    @Value("${jwt.refresh-token-time}")
    private long refreshTokenTime;

    public String createAccessToken(Long userId) {
        return createToken(userId, accessTokenTime);
    }

    public String createRefreshToken(Long userId) {
        return createToken(userId, refreshTokenTime);
    }

    private String createToken(Long userId, long validTime) {
        Date now = new Date();
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .claim("userId", userId)
                .compact();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException();
        }
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new RuntimeException();
        }
        return true;
    }

    public Long parseUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
        return claims.get("id", Long.class);
    }
}
