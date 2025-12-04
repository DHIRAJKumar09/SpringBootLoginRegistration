package com.tcs.registration_login.userauthentication.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.audience}")
    private String audience;
    @Value("${jwt.expiration-ms}")
    private long validityInMs;

    private Key key;

    @PostConstruct
    public void init(){
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    public String createToken(String username, List<String> roles){
        Claims claims = Jwts.claims().setSubject(username);
        if(roles != null && !roles.isEmpty()){
            claims.put("roles",roles);
        }
        Date now  = new Date();
        Date exp = new Date(now.getTime()+validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setAudience(audience)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public Jws<Claims> validateTokenAndGetCalims(String token) throws JwtException{
        return Jwts.parserBuilder()
                .requireIssuer(issuer)
                .requireAudience(audience)
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
    public String getUsernameFromClaims(Claims claims) {
        return claims.getSubject();
    }
   @SuppressWarnings("unchecked")
   public List<String> getRolesFromClaims(Claims claims) {
       Object roles = claims.get("roles");
       if (roles instanceof List) {
        return ((List<?>) roles).stream().map(Object::toString).collect(Collectors.toList());
       }
       return List.of();
    }


}
