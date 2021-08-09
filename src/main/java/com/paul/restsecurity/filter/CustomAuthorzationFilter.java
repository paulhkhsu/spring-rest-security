package com.paul.restsecurity.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paul.restsecurity.dto.JWTTokenUserInfo;
import com.paul.restsecurity.util.AppConstants;
import com.paul.restsecurity.util.JWTUtils;
import com.paul.restsecurity.util.ResponseUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorzationFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String url = request.getServletPath();
		if ("/api/login".equals(url) || "/api/token/refresh".equals(url)) {
			filterChain.doFilter(request, response);
		} else {
			String authorzationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (null != authorzationHeader && authorzationHeader.startsWith(AppConstants.BEARER)) {
				try {
					String token = authorzationHeader.substring(AppConstants.BEARER.length());
					JWTTokenUserInfo jwtTokenUserInfo = JWTUtils.JWTTokenInfo(token);
					Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
					Arrays.asList(jwtTokenUserInfo.getRoles()).stream()
							.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							jwtTokenUserInfo.getUsername(), null, authorities);
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					filterChain.doFilter(request, response);
				} catch (Exception ex) {
					log.error("Error log in {} ", ex.getMessage());
					ResponseUtils.ReplyError(response, "Failed to authenticate user", HttpStatus.FORBIDDEN);
				}
			} else {
				filterChain.doFilter(request, response);
			}
		}

	}

}
