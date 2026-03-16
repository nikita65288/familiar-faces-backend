package com.github.nikita65288.jwt;

import com.github.nikita65288.exception.FFIllegalStateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtProvider {

    private final SecretKey key;
    private final Long defaultExpiration;

    public JwtProvider(String secret, long defaultExpiration) {
        this.key = getSignKey(secret);
        this.defaultExpiration = defaultExpiration;
    }

    public JwtProvider(String secret) {
        this.key = getSignKey(secret);
        this.defaultExpiration = null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public String generateToken(Map<String, Object> extraClaims, String subject, Long expiration) {
        var builder = Jwts.builder()
                .claims(extraClaims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key);

        // Set subject only if it is passed
        if (subject != null && !subject.isBlank()) {
            builder.subject(subject);
        }

        return builder.compact();
    }

    public String generateToken(Map<String, Object> extraClaims, String subject) {
        if (defaultExpiration == null) {
            throw new FFIllegalStateException("Default expiration is not configured for this JwtProvider instance.");
        }
        return generateToken(extraClaims, subject, defaultExpiration);
    }

    public String generateToken(String subject) {
        if (defaultExpiration == null) {
            throw new FFIllegalStateException("Default expiration is not configured for this JwtProvider instance.");
        }
        return generateToken(new HashMap<>(), subject, defaultExpiration);
    }

    /**
     * Extracts any claim from token and converts it to the required type.
     */
    public <T> T getClaim(String token, String claimKey, Class<T> requiredType) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimKey, requiredType);
    }

    public String getSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    // region Private helpers
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // endregion Private helpers
}
