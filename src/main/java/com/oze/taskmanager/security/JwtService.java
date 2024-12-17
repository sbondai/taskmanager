package com.oze.taskmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class JwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final SignatureAlgorithm signatureAlgorithm;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public JwtService(
        @Value("${jwt.private-key}") String privateKeyPath,
        @Value("${jwt.public-key}") String publicKeyPath,
        @Value("${jwt.signature-algorithm:RS256}") String algorithm) throws Exception {
        this.privateKey = loadKey(privateKeyPath, true);
        this.publicKey = loadKey(publicKeyPath, false);
        this.signatureAlgorithm = SignatureAlgorithm.forName(algorithm);

        log.info("Private Key Algorithm: {}", this.privateKey.getAlgorithm());
        log.info("Public Key Algorithm: {}", this.publicKey.getAlgorithm());
        log.info("JWT Signature Algorithm: {}", this.signatureAlgorithm.getValue());
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        String token = Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(privateKey, signatureAlgorithm)
            .compact();

        log.info("Generated Token for user '{}': {}", subject, token);
        return token;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(publicKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        try {
            return extractUsername(token).equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private <T> T loadKey(String resourcePath, boolean isPrivateKey) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath.replace("classpath:", ""))) {
            if (is == null) throw new IllegalArgumentException("Key file not found: " + resourcePath);

            byte[] keyBytes = is.readAllBytes();
            KeyFactory kf = KeyFactory.getInstance("RSA");

            if (isPrivateKey) {
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
                return (T) kf.generatePrivate(spec);
            } else {
                X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
                return (T) kf.generatePublic(spec);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load key from " + resourcePath, e);
        }
    }
}
