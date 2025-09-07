package com.doan.nhansu.admin.service;

import com.doan.nhansu.admin.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    protected long ACCESS_TOKEN_VALIDITY =  7 * 24 * 60 * 60 * 1000; // 15 phút
    protected long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 ngày
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    HttpServletRequest request;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserResponse userResponse) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("type", "access");
        extraClaims.put("payload",userResponse);
        return createToken(extraClaims, userResponse,ACCESS_TOKEN_VALIDITY);
    }
    public String generateRefreshToken(UserResponse userResponse) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, userResponse, REFRESH_TOKEN_VALIDITY);
    }

    private boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String createToken(Map<String, Object> claims, UserResponse userResponse, long validity) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userResponse.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SignatureAlgorithm.HS256, SIGNER_KEY)
                .compact();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SIGNER_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public UserResponse getUserResponseFromToken(String token){
        UserResponse UserResponse = null;
        try {
            Claims claims = this.extractAllClaims(token);
            Object info =  claims.get("payload");
            String infoJson = objectMapper.writeValueAsString(info);
            UserResponse = objectMapper.readValue(infoJson, UserResponse.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return UserResponse;
    }
    public String getTokenStringFromHttpRequest(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        final String token ;
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            return null;
        }
        token = authHeader.substring(7);
        return token != null ? token : null;
    }
    public UserResponse getPrincipal(){
        String token = getTokenStringFromHttpRequest(request);
        if (token == null) {
            return null;
        }
        return getUserResponseFromToken(token);
    }
}
