package com.leka.blogteashop.service.jwt;

import com.leka.blogteashop.service.jwt.config.JwtConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultJwtParserBuilder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Jwt Service class implements main operations with jwt, like creating, extracting claims etc.
 *
 * @author osynelnyk
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfiguration.JwtProperties jwtProperties;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenExpired(String jwtToken) {
        return getExpirationDate(jwtToken).before(new Date());
    }

    /**
     * Retrieves all claims from a jwt token.
     *
     * @param jwtToken string representation of the jwt token.
     * @return claims that holds specific information about the user etc.
     */
    public Claims getAllClaims(String jwtToken) {
        return new DefaultJwtParserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private Date getExpirationDate(String jwtToken) {
        return getClaims(jwtToken, Claims::getExpiration);
    }

    private <T> T getClaims(String jwtToken, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }
}
