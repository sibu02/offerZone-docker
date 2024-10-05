package com.example.offerZone.utility;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.offerZone.exception.UserException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	private String secret_key = "MY_SECRET_KEY";
	
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token,Claims::getExpiration);
	}
	
	public <T> T  extractClaim(String token,Function<Claims,T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {  
		return Jwts.parser()
					.setSigningKey(secret_key)
					.parseClaimsJws(token)
					.getBody();
	}
	
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public String generateToken(Authentication authentication) throws UserException {
		System.out.println(authentication.getPrincipal());
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		try {
			System.out.println(userDetails.getUsername());
		}
		catch(Exception e) {
			throw new UserException("Not wrking");
		}
//		System.out.println(userDetails.getUsername());
		Map<String,Object> claims = new HashMap<>();
		return createToken(userDetails.getUsername(),claims);
	}

	private String createToken(String subject, Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims)
					.setSubject(subject)
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
					.signWith(SignatureAlgorithm.HS256, secret_key).compact();
					
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		final String email = extractEmail(token);
		return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
