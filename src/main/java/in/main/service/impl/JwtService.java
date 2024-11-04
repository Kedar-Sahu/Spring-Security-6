package in.main.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private String secretKey="";
	
	public String generateToken(String username) {
		Map<String,Object> map=new HashMap<>();
		map.put("username", username);
		
		String token=Jwts.builder()
		.claims()
		.add(map)
		.subject(username)
		.issuedAt(new Date(System.currentTimeMillis()))
		.expiration(new Date(System.currentTimeMillis()+60*60*10))
		.and()
		.signWith(getKey())
		.compact();
		
		return token;
	}

	private Key getKey() {
		try {
			KeyGenerator key=KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk=key.generateKey();
			secretKey=Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		byte[] keyBytes=Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	private Claims exrtactAllClaims(String token) {
		Claims claims=Jwts.parser().verifyWith(decryptKey(secretKey)).build()
		.parseSignedClaims(token)
		.getPayload();
		
		return claims;
	}
	
	private <T> T extractClaim(String token,Function<Claims,T> claimResolver) {
		Claims claims= exrtactAllClaims(token);
		return claimResolver.apply(claims);
	}

	private SecretKey decryptKey(String secretKey2) {
	    byte[] keyBytes= Decoders.BASE64.decode(secretKey);
	    return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUsername(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	
	private Date extractExpiration(String token) {
		return extractClaim(token,Claims::getExpiration);
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		String username=extractUsername(token);
		Boolean expired=isTokenExpired(token);
		if(username.equals(userDetails.getUsername()) && !expired) {
			return true;
		}
		return false;
	}

	private Boolean isTokenExpired(String token) {
		Date expiredDate=extractExpiration(token);
		return expiredDate.before(new Date());
	}
}
