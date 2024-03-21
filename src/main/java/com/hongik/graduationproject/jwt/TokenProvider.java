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

@Component
public class TokenProvider implements InitializingBean {

    private static final long REFRESH_TOKEN_EXPIRATION = 604800000;

    private static final String SECURITY_KEY = "jwtseckey!@";

    public String create (Long id){
        Date exprTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        Date refreshExprTime = Date.from(Instant.now().plus(REFRESH_TOKEN_EXPIRATION, ChronoUnit.MILLIS));

        String refreshToken = Jwts.builder()
                .setExpiration(refreshExprTime)
                .signWith(SignatureAlgorithm.HS512, SECURITY_KEY)
                .compact();

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECURITY_KEY)
                .setIssuedAt(new Date())
                .setExpiration(exprTime)
                .claim("refreshToken", refreshToken)
                .claim("id", id)
                .compact();
    }

    public String validate(String token) {
        try{
            Claims claims = Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(token).getBody();

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

    public String refresh(String accessToken){
        try {
            Claims claims = Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(accessToken).getBody();

            validateExpiration(claims);

            Date newExpirationTime = Date.from(Instant.now().plus(REFRESH_TOKEN_EXPIRATION, ChronoUnit.MILLIS));
            String refreshToken = claims.get("refreshToken", String.class);

            if (refreshToken != null) {
                return Jwts.builder()
                        .setClaims(claims)
                        .setExpiration(newExpirationTime)
                        .signWith(SignatureAlgorithm.HS512, SECURITY_KEY)
                        .compact();
            } else {
                throw new RuntimeException("No existing refreshToken found for refresh");
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Token refresh failed", e);
        }
    }

    public Long getUserId(String token){
        Claims claims = Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(token).getBody();
        Long id = claims.get("id", Long.class);
        return id;
    }
}
