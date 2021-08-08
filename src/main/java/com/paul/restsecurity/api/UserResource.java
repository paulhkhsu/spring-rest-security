package com.paul.restsecurity.api;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paul.restsecurity.domain.AppUser;
import com.paul.restsecurity.domain.Role;
import com.paul.restsecurity.service.UserService;
import com.paul.restsecurity.util.AppConstants;
import com.paul.restsecurity.util.JWTUtils;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserResource {
	private final UserService userService;

	@GetMapping("/users")
	public ResponseEntity<List<AppUser>> getUsers() {
		return ResponseEntity.ok().body(userService.getUsers());
	}

	@PostMapping("/user/save")
	public ResponseEntity<AppUser> savetUser(AppUser appUser) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
		return ResponseEntity.created(uri).body(userService.saveUser(appUser));
	}

	@PostMapping("/role/save")
	public ResponseEntity<Role> saveRole(Role role) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
	}

	@PostMapping("/role/addtouser")
	public ResponseEntity<Role> addRoleToUser(RoleToUserForm form) {
		userService.addRoleToUser(form.getUsername(), form.getRolename());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		String authorzationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (null != authorzationHeader && authorzationHeader.startsWith(AppConstants.BEARER)) {
			try {
				// verify bearer token
				String refreshToken = authorzationHeader.substring(AppConstants.BEARER.length());
				DecodedJWT decodeJWT = JWTUtils.validateJWTToken(refreshToken);
				// get APP USer
				String username = decodeJWT.getSubject();
				AppUser appUser = userService.getUser(username);
				// create access token
				String accessToken = JWTUtils.createAccessToken(appUser, request);
				// refresh token from input, no need to create
				Map<String, String> tokens = JWTUtils.createTokenMap(accessToken, refreshToken);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);

			} catch (Exception ex) {
				log.error("Error log in {} ", ex.getMessage());
				response.setHeader("error", ex.getMessage());
				response.setStatus(HttpStatus.FORBIDDEN.value());
				// response.sendError(HttpStatus.FORBIDDEN.value());
				Map<String, String> error = new HashMap<>();
				error.put("error_message", ex.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		} else {
			throw new RuntimeException("Refresh token is missing");
		}
	}
}

@Data
class RoleToUserForm {

	private String username;
	private String rolename;

}
