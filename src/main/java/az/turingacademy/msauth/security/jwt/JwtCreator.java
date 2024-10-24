package az.turingacademy.msauth.security.jwt;

import az.turingacademy.msauth.dao.entity.UserEntity;
import az.turingacademy.msauth.model.enums.TokenType;
import az.turingacademy.msauth.util.PublicPrivateKeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtCreator {

    private static final List<GrantedAuthority> AUTHORITIES = List.of(new SimpleGrantedAuthority("USER"));

    @Value("${application.security.jwt.expiration}")
    private String accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private String refreshTokenExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Claims extractAllClaims(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(PublicPrivateKeyUtils.KEY_PAIR.getPublic())
                .verifyWith(PublicPrivateKeyUtils.KEY_PAIR.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        log.info("Extracted Claims: " + claims);

        return claims;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims); //??
    }

    public String generateToken(Map<String, Object> extraClaims, Authentication authentication, TokenType tokenType) {
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        UserDetails principal = (UserDetails) authentication.getPrincipal();

        return Jwts
                .builder()
                .claims()
                .empty()
                .add(extraClaims)
                .add("auth", authorities)
                .add("token_type", tokenType)
                .subject(principal.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (tokenType == TokenType.ACCESS ?
                        Long.parseLong(accessTokenExpiration) : Long.parseLong(refreshTokenExpiration))))
                .and()
                .signWith(PublicPrivateKeyUtils.KEY_PAIR.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    public String generateToken(Authentication authentication, TokenType tokenType) {
        return generateToken(new HashMap<>(), authentication, tokenType);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date exp = extractClaim(token, Claims::getExpiration);
        return exp.before(new Date());
    }

    public Authentication parseAuthentication(String authToken, UserDetails userDetails) {
        isTokenValid(authToken, userDetails);
        final Claims claims = extractClaim(authToken);
        final UserDetails principal = getPrincipal(claims);
        return new UsernamePasswordAuthenticationToken(principal, authToken, AUTHORITIES);
    }

    private Claims extractClaim(String authToken) {
        return Jwts.parser()
                .setSigningKey(PublicPrivateKeyUtils.KEY_PAIR.getPublic())
                .build()
                .parseSignedClaims(authToken)
                .getPayload();
    }

    private UserDetails getPrincipal(Claims claims) {
        String subject = claims.getSubject();
        String fullName = claims.get("full_name", String.class);
        String auth = claims.get("auth", String.class);
        return new UserEntity(subject, fullName, auth);
    }

}