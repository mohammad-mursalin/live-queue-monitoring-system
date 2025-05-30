package com.mursalin.queue_monitoring_system.jwt;

import com.mursalin.queue_monitoring_system.model.User;
import com.mursalin.queue_monitoring_system.model.UserPrinciples;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    // Generate token with user ID as subject, and other claims like username, role, phone
    public String generateToken(@NonNull User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getName());
        claims.put("role", user.getRole());
        claims.put("phone", user.getPhone());

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(String.valueOf(user.getId())) // user ID as subject
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 200 * 60 * 1000)) // e.g., 200 minutes expiry
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException ex) {
            throw new JwtException("Invalid JWT secret key", ex);
        }
    }

    // Extract user ID (subject) from token
    public String extractUserId(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception ex) {
            throw new JwtException("Failed to extract user ID from token", ex);
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new JwtException("JWT token has expired", ex);
        } catch (MalformedJwtException ex) {
            throw new JwtException("Invalid JWT token", ex);
        } catch (SignatureException ex) {
            throw new JwtException("JWT signature validation failed", ex);
        } catch (Exception ex) {
            throw new JwtException("Failed to parse JWT token", ex);
        }
    }

    // Validate token by matching extracted user ID with the user details fetched from DB
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String userIdFromToken = extractUserId(token);
            String userIdFromUserDetails = ((UserPrinciples) userDetails).getId();

            return (userIdFromToken.equals(userIdFromUserDetails) && !isTokenExpired(token));
        } catch (JwtException ex) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

