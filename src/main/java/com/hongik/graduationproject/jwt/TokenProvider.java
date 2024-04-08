package com.hongik.graduationproject.jwt;

import com.hongik.graduationproject.exception.AppException;
import com.hongik.graduationproject.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider implements InitializingBean {

    private static final long REFRESH_TOKEN_EXPIRATION = 604800000;

    private static final String SECURITY_KEY = "jwtseckey!@"; //숨기기

    public String createAccessToken(Long id){
        Date exprTime = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECURITY_KEY)
                .setIssuedAt(new Date())
                .setExpiration(exprTime)
                .claim("id", id)
                .compact();
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
            throw new AppException(ErrorCode.REFRESH_TOKEN_CREATE_FAILED);
        }
    }

    public String validate(String token) {
        try{
            Claims claims = Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(token).getBody();
            validateExpiration(claims);
            return claims.getSubject();
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_VALIDATION_FAILED);
        }
    }

    private void validateExpiration(Claims claims) {
        Date expiration = claims.getExpiration();
        if (expiration != null && expiration.before(new Date())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public Long getUserId(String token){
        Claims claims = Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(token).getBody();
        return claims.get("id", Long.class);
    }
}
