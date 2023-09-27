package com.ama.imanuel.connectwithmebe.config;

import com.ama.imanuel.connectwithmebe.shared.TokenHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.refresh-key}")
    private String secretKeyRefresh;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.expiration-refresh}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUsername(String token, String key) {
        return extractClaim(token, Claims::getSubject, key);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String key){
        final Claims claims = extractAllClaims(token, key);
        return claimsResolver.apply(claims);
    }

    public TokenHolder generateToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails
    ){
        return buildToken(extractClaims, userDetails, jwtExpiration);
    }

    public TokenHolder generateRefreshToken(UserDetails userDetails){
        return buildToken(new HashMap<>(), userDetails, refreshExpiration, secretKeyRefresh);
    }

    public TokenHolder buildToken(Map<String, Object> extractClaims, UserDetails userDetails, long expiration) {
        var timeNow = new Date(System.currentTimeMillis());
        var expiredAt = new Date(timeNow.getTime() + expiration);
        String token = Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(timeNow)
                .setExpiration(expiredAt)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        return new TokenHolder(token, expiredAt.getTime());
    }

    public TokenHolder buildToken(Map<String, Object> extractClaims, UserDetails userDetails, long expiration, String key) {
        var timeNow = new Date(System.currentTimeMillis());
        var expiredAt = new Date(timeNow.getTime() + expiration);
        String token = Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(timeNow)
                .setExpiration(expiredAt)
                .signWith(getSignInKey(key), SignatureAlgorithm.HS256)
                .compact();
        return new TokenHolder(token, expiredAt.getTime());
    }

    public TokenHolder generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public boolean isTokenValid(String token, UserDetails userDetails, String key){
        final String username = extractUsername(token, key);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, key);
    }

    private boolean isTokenExpired(String token, String key) {
        return extractExpiration(token, key).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Date extractExpiration(String token, String key) {
        return extractClaim(token, Claims::getExpiration, key);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims extractAllClaims(String token, String key){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getSignInKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
