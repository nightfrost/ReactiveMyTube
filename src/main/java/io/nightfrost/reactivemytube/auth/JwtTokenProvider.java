package io.nightfrost.reactivemytube.auth;

import io.jsonwebtoken.*;
import io.nightfrost.reactivemytube.exceptions.ConfigurationException;
import io.nightfrost.reactivemytube.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.util.Logger;
import reactor.util.Loggers;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final Logger LOGGER = Loggers.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.salt}")
    private String jwtSalt;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public String generateToken(Authentication authentication) {

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        LOGGER.info("Generating token for " + userPrincipal.getUsername());
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration * 1000L))
                .signWith(getKeyFromPassword())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKeyFromPassword())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getKeyFromPassword()).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            LOGGER.error("Failed to validate token.", e);
            return false;
        }
    }

    private SecretKey getKeyFromPassword() {
        SecretKeySpec secretKeySpec;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(jwtSecret.toCharArray(), jwtSalt.getBytes(), 65536, 256);
            secretKeySpec = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "HmacSHA256");
            return secretKeySpec;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            if (e instanceof InvalidKeySpecException) throw new ValidationException(e.getMessage());
            throw new ConfigurationException(e.getMessage());
        }
    }

    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getKeyFromPassword()).build().parseClaimsJws(token).getBody();

        return claims.get("roles", List.class);
    }
}
