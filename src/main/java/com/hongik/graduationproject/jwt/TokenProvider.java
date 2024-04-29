package com.hongik.graduationproject.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class TokenProvider implements InitializingBean {
    @Value("${jwt.secret}")
    private String jwtSecretKey;
    @Value("${jwt.access-token-time}")
    private long accessTokenTime;
    @Value("${jwt.refresh-token-time}")
    private long refreshTokenTime;

    public String createAccessToken(Long userId){
        return createToken(userId, accessTokenTime);
    }

    public String createRefreshToken(Long userId){
        return createToken(userId, refreshTokenTime);
    }

    private String createToken(Long userId, long validTime) {
        Date now = new Date();
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenTime))
                .claim("userId", userId)
                .compact();
    }

    public String validate(String token) {
        try{
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

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public Long getUserId(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
        return claims.get("id", Long.class);
    }
}
