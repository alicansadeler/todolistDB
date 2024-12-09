package com.alicansadeler.todolist.service;

import com.alicansadeler.todolist.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

// ADIM 1
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername()) //token sahibi username belirler
                .claim("role", user.getRole().name()) // ek bilgi(rol) ekler
                .setIssuedAt(new Date(System.currentTimeMillis())) // Token zamanı
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Token ne kadar geçerli
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Token'ı belirtilen algoritma (HS256) ile imzalar
                .compact();
    }

    private Key getSignInKey() {
        // imza için key olusturur
        // SECRET_KEY değişkenini Base64 formatından çözüp HMAC algoritması için bir anahtar üretir.
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


// ADIM 2

    private Claims extractAllClaims(String token) {
        //Token'ı çözerek içinde saklanan tüm Claims bilgilerini döndürür.
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Token'dan bir özelliği (örneğin kullanıcı adı, rol, vb.) dinamik olarak çıkarır.
        final Claims claims = extractAllClaims(token);  // Tüm bilgileri al
        return claimsResolver.apply(claims);            // İstenen bilgiyi döndür
    }

    public String extractUsername(String token) {
        // Token'dan username bilgisini çıkarır. Bu bilgi token'ın setSubject kısmında belirtilmiştir
        return extractClaim(token, Claims::getSubject);
    }



// ADIM-3
    private Date extractExpiration(String token) {
        // Token'ın sona erme tarihini çıkarır (Claims::getExpiration fonksiyonuyla).
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        // Token'ın süresinin dolup dolmadığını kontrol eder.
        return extractExpiration(token).before(new Date());
    }


// ADIM-4
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}
