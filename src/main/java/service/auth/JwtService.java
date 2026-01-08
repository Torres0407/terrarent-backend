package service.auth;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import model.entity.User;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//import static io.jsonwebtoken.Jwts.*;
//
//@Service
//public class JwtService {
//
//    private final String SECRET = "VERY_SECURE_SECRET_KEY";
//    private final long ACCESS_EXP = 1000 * 60 * 15;
//    private final long REFRESH_EXP = 1000 * 60 * 60 * 24 * 7;
//
//    public String generateAccessToken(User user) {
//        return builder()
//                .setSubject(user.getEmail())
//                .claim("role", user.getRole().name())
//                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
//                .signWith(SignatureAlgorithm.HS256, SECRET)
//                .compact();
//    }
//
//    public String generateRefreshToken(User user) {
//        return builder()
//                .setSubject(user.getEmail())
//                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
//                .signWith(SignatureAlgorithm.HS256, SECRET)
//                .compact();
//    }
//
//    public String extractEmail(String token) {
//        return parser().setSigningKey(SECRET)
//                .parseClaimsJws(token).getBody().getSubject();
//    }
//}
//


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import model.entity.User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "VERY_SECURE_SECRET_KEY_MUST_BE_AT_LEAST_32_CHARS!";
    private final long ACCESS_EXP = 1000 * 60 * 15;
    private final long REFRESH_EXP = 1000L * 60 * 60 * 24 * 7;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
