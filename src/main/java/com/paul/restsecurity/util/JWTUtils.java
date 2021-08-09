package com.paul.restsecurity.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.paul.restsecurity.domain.AppUser;
import com.paul.restsecurity.domain.Role;
import com.paul.restsecurity.dto.JWTTokenUserInfo;

@Component
public class JWTUtils {
	static Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
	static JWTVerifier verifier = JWT.require(algorithm).build();

	public static DecodedJWT validateJWTToken(String token) {
		return verifier.verify(token);

	}

	public static DecodedJWT validateBearerJWTToken(String bearer) {
		String token = bearer.substring(AppConstants.BEARER.length());
		return verifier.verify(token);

	}

	public static String createAccessToken(User user, HttpServletRequest request) {
		String[] roles = user.getAuthorities().stream().map(a -> a.getAuthority()).toArray(n -> new String[n]);
		String accessToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString()).withArrayClaim(AppConstants.ROLES, roles)
				.sign(algorithm);

		return accessToken;
	}

	public static String createAccessToken(AppUser user, HttpServletRequest request) {
		String[] roles = user.getRoles().stream().map(Role::getName).toArray(n -> new String[n]);
		String accessToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString()).withArrayClaim(AppConstants.ROLES, roles)
				.sign(algorithm);

		return accessToken;
	}

	public static String createRefreshToken(User user, HttpServletRequest request) {
		String refreshToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
				.withIssuer(request.getRequestURL().toString()).sign(algorithm);
		return refreshToken;
	}

	public static Map<String, String> createTokenMap(String accessToken, String refreshToken) {
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", accessToken);
		tokens.put("refresh_token", refreshToken);
		return tokens;
	}

	public static JWTTokenUserInfo JWTTokenInfo(String token) {
		DecodedJWT decodeJWT = JWTUtils.validateJWTToken(token);
		String username = decodeJWT.getSubject();
		String[] roles = decodeJWT.getClaims().get(AppConstants.ROLES).asArray(String.class);
		JWTTokenUserInfo userInfo = new JWTTokenUserInfo();
		userInfo.setRoles(roles);
		userInfo.setUsername(username);
		return userInfo;

	}

}
