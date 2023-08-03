package npc.kassinimvp.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import npc.kassinimvp.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
    @Value("${service.security.secret}")
    private String jwtSecret;

    @Value("${service.security.expirationMS}")
    private String jwtExpirationMS;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserEmailFromJwtToken(String authToken) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken).getBody().getSubject();
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
            .setSubject(userPrincipal.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMS))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch(MalformedJwtException ex) {
            log.error("Invalid JSON web token: {}", ex.getMessage());
        } catch(ExpiredJwtException ex) {
            log.error("JSON web token is expired: {}", ex.getMessage());
        } catch(UnsupportedJwtException ex) {
            log.error("JSON web token is unsupported: {}", ex.getMessage());
        } catch(IllegalArgumentException ex) {
            log.error("JSON web token claims string is empty: {}", ex.getMessage());
        }

        return false;
    }
}
