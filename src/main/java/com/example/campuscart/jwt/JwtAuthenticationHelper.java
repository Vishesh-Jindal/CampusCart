package com.example.campuscart.jwt;

import com.example.campuscart.constants.Constants;
import com.example.campuscart.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationHelper {
    public Claims getClaims(String token){
        try{
            return Jwts.parserBuilder().setSigningKey(Constants.SECERET_KEY.getBytes())
                    .build().parseClaimsJws(token).getBody();
        } catch (Exception e){
            throw new InvalidTokenException("Token Parsing failed due to:"+e);
        }
    }

    public boolean isTokenExpired(String token){
        Claims claims = this.getClaims(token);
        return claims.getExpiration().before(new Date());
    }
    public String generateToken(String email){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+Constants.JWT_TOKEN_VALIDITY*1000))
                .signWith(new SecretKeySpec(Constants.SECERET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName()),SignatureAlgorithm.HS512)
                .compact();
    }
}
