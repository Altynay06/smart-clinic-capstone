package com.project.back_end.services;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
@Service
public class TokenService {
 private final SecretKey key;
 public TokenService(@Value("${jwt.secret:}") String secret){key=secret.isBlank()?Jwts.SIG.HS256.key().build():Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));}
 public String generateToken(Long id,String role){return Jwts.builder().subject(id.toString()).claim("role",role).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+3600000)).signWith(key).compact();}
 public Claims validateToken(String header,String... roles){
  try {if(header==null||!header.startsWith("Bearer "))throw new IllegalArgumentException();
   Claims claims=Jwts.parser().verifyWith(key).build().parseSignedClaims(header.substring(7)).getPayload();
   if(roles.length>0&&!java.util.Arrays.asList(roles).contains(claims.get("role",String.class)))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Insufficient role");
   return claims;
  }catch(ResponseStatusException error){throw error;}catch(Exception error){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Valid bearer token required");}
 }
 public Long identity(String header,String... roles){return Long.valueOf(validateToken(header,roles).getSubject());}
}
