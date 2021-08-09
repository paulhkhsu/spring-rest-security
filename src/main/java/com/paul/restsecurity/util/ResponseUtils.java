package com.paul.restsecurity.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtils {
	public static void ReplyError(HttpServletResponse response, String errormsg, HttpStatus status)
			throws JsonGenerationException, JsonMappingException, IOException {
		response.setStatus(status.value());
		Map<String, String> error = new HashMap<>();
		error.put("error_message", errormsg);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), error);
	}

	public static void ReplyOk(HttpServletResponse response, Map<String, String> body)
			throws JsonGenerationException, JsonMappingException, IOException {
		response.setStatus(HttpStatus.OK.value());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), body);
	}

}
