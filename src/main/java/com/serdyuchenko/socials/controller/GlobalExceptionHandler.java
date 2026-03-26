package com.serdyuchenko.socials.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Глобальный обработчик REST-исключений.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, String>> catchDataIntegrityViolationException(
		final DataIntegrityViolationException exception,
		final HttpServletRequest request
	) {
		final Map<String, String> details = new HashMap<>();
		details.put("message", exception.getMostSpecificCause().getMessage());
		details.put("path", request.getRequestURI());
		return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
	}
}
