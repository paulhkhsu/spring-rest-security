package com.paul.restsecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHelper {

	@ExceptionHandler(value = { Exception.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Object> handleException(Exception ex) {
		log.error("Exception: in ExceptionHelper ", ex.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}
}
