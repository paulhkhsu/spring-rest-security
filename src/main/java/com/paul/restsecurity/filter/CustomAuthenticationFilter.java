package com.paul.restsecurity.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paul.restsecurity.util.JWTUtils;
import com.paul.restsecurity.util.ResponseUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userName, password);
		return authenticationManager.authenticate(upat);

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		User user = (User) authResult.getPrincipal();
		String accessToken = JWTUtils.createAccessToken(user, request);
		String refreshToken = JWTUtils.createRefreshToken(user, request);
		// pass token in header
		// response.setHeader("access_token", accessToken);
		// response.setHeader("refresh_token", refreshToken);

		// pass token in body
		Map<String, String> tokens = JWTUtils.createTokenMap(accessToken, refreshToken);
		ResponseUtils.ReplyOk(response, tokens);
		}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.error("Failed unsuccessfulAuthentication");
		ResponseUtils.ReplyError(response, "Failed to authenticate user", HttpStatus.UNAUTHORIZED);
	}

}
