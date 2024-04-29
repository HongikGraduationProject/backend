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

    public String createRefreshToken(String accessToken){
        try {
            Claims claims = Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(accessToken).getBody();

            validateExpiration(claims);

            Date newExpirationTime = Date.from(Instant.now().plus(REFRESH_TOKEN_EXPIRATION, ChronoUnit.MILLIS));

            String refreshToken = Jwts.builder()
                    .setExpiration(newExpirationTime)
                    .signWith(SignatureAlgorithm.HS512, SECURITY_KEY)
                    .compact();

            claims.put("refreshToken", refreshToken);

            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(newExpirationTime)
                    .signWith(SignatureAlgorithm.HS512, SECURITY_KEY)
                    .compact();
        } catch (Exception e) {
            throw new BadCredentialsException("Token refresh failed", e);
        }
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
