package com.examportal.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.function.Function;

import io.jsonwebtoken.Claims;

@Service
public class JwtService {

	// Secret key used to sign and verify JWT tokens
	@Value("${jwt.secret}")
	private String secretKey;

	// Token validity duration in milliseconds
	@Value("${jwt.expiration}")
	private long jwtExpiration;

	// Converts the Base64 encoded secret into a SecretKey object
	private SecretKey getSigningKey() {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);

		return Keys.hmacShaKeyFor(keyBytes);
	}

	// Generates a JWT token for the authenticated user
	public String generateToken(String email) {

		return Jwts.builder()

				// Stores the logged-in user's email
				.subject(email)

				// Token creation time.
				.issuedAt(new Date())

				// Token expiry time.
				.expiration(new Date(System.currentTimeMillis() + jwtExpiration))

				// Signs the token using the application's secret key
				.signWith(getSigningKey())

				// Converts everything into the final JWT string
				.compact();
	}
	
	// Extracts the email (subject) stored inside the JWT
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	// Reads any claim from the JWT using the resolver function
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

		final Claims claims = extractAllClaims(token);

		return claimsResolver.apply(claims);
	}
	
	// Parses the JWT and returns all the claims stored inside it
	private Claims extractAllClaims(String token) {

		return Jwts.parser()

				// Uses our secret key to verify the JWT signature.
				.verifyWith(getSigningKey())

				// Builds the JWT parser
				.build()

				// Parses the signed JWT.
				.parseSignedClaims(token)

				// Returns the payload (claims) from the JWT
				.getPayload();
	}
	
	// Checks whether the JWT has already expired
	private boolean isTokenExpired(String token) {

		// Token is expired if the expiry time is before the current time
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}
	
	// Validates whether the token belongs to the given user and is still valid
	public boolean isTokenValid(String token, UserDetails userDetails) {

		// Email stored inside the JWT
		final String username = extractUsername(token);

		// Token is valid only if the email matches and it has not expired
		return username.equals(userDetails.getUsername())
				&& !isTokenExpired(token);
	}

}