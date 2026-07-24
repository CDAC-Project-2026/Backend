package com.examportal.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	// Secret key used to sign and verify JWT tokens.
	@Value("${jwt.secret}")
	private String secretKey;

	// Token validity duration in milliseconds.
	@Value("${jwt.expiration}")
	private long jwtExpiration;

	// Converts the Base64 encoded secret into a SecretKey object.
	private SecretKey getSigningKey() {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);

		return Keys.hmacShaKeyFor(keyBytes);
	}

	// Generates a JWT token for the authenticated user.
	public String generateToken(String email) {

		return Jwts.builder()

				// Stores the logged-in user's email.
				.subject(email)

				// Token creation time.
				.issuedAt(new Date())

				// Token expiry time.
				.expiration(new Date(System.currentTimeMillis() + jwtExpiration))

				// Signs the token using the application's secret key.
				.signWith(getSigningKey())

				// Converts everything into the final JWT string.
				.compact();
	}

}