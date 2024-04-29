package com.hongik.graduationproject.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

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

    public String validate(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
            validateExpiration(claims);
            return claims.getSubject();
        } catch (Exception e) {
            throw new BadCredentialsException("JWT validation failed", e);
        }
    }

    private void validateExpiration(Claims claims) {
        Date expiration = claims.getExpiration();
        if (expiration != null && expiration.before(new Date())) {
            throw new BadCredentialsException("JWT has expired");
        }
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
        return claims.get("id", Long.class);
    }
}
