package com.example.stylescanner.jwt.provider;

import com.example.stylescanner.jwt.dto.JwtDto;
import com.example.stylescanner.security.service.JpaUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String salt;

    private Key secretKey;

    // 만료시간 : 1Hour
    private final long exp = 1000L * 60 * 60;
    private final long refreshExp = 1000L * 60 * 60 * 24 * 14; // 14 days in milliseconds

    private final JpaUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String account) {
        Claims claims = Jwts.claims().setSubject(account);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String account) {
        Date now = new Date();

        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + refreshExp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * AccessToken, RefreshToken 생성 메소드
     * @param account
     * @return JWT토큰
     */
    public String createToken(String account) {
        Claims claims = Jwts.claims().setSubject(account);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

    }

    /**
     * JWT 토큰에서 권한 인증하는 메소드
     * @param token
     * @return 인증 정보
     */

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccount(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * JWT 토큰에서 email 가져오는 메소드
     * @param token
     * @return email
     */
    public String getAccount(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Http header에서 JWT 토큰을 가져오는 메소드
     * @param request
     * @return JWT 토큰
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    /**
     * JWT 유효성 검사 메소드
     * @param token
     * @return 유효성 유무
     */
    public boolean validateToken(String token) {
        try {
            // Bearer 검증
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            // 만료되었을 시 claims 객체가 안만들어짐, false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * JWT 토큰 남은 만료 시간
     * @param accessToken
     * @return 남은 시간
     */
    public long getExpiration(String accessToken) {
        Date expriration =  Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();

        long now = new Date().getTime();
        return expriration.getTime() - now;
    }

    /**
     * RefreshToken으로 accessToken 재발급 하는 메소드
     * @param refreshToken
     * @return
     */
    public String reissueToken(String refreshToken) {
        String email = getAccount(refreshToken);
        return generateAccessToken(email);
    }

}