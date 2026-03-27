package com.serdyuchenko.socials.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.serdyuchenko.socials.validation.ValidationErrorResponse;
import com.serdyuchenko.socials.validation.Violation;

/**
 * Глобальный обработчик REST-исключений.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> catchMethodArgumentNotValidException(
		final MethodArgumentNotValidException exception
	) {
		final List<Violation> violations = exception.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> new Violation(error.getField(), error.getDefaultMessage()))
			.toList();
		return new ResponseEntity<>(new ValidationErrorResponse(violations), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ValidationErrorResponse> catchConstraintViolationException(
		final ConstraintViolationException exception
	) {
		final List<Violation> violations = exception.getConstraintViolations()
			.stream()
			.map(violation -> new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
			.toList();
		return new ResponseEntity<>(new ValidationErrorResponse(violations), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> catchIllegalArgumentException(
		final IllegalArgumentException exception,
		final HttpServletRequest request
	) {
		return new ResponseEntity<>(buildDetails(exception.getMessage(), request), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, String>> catchDataIntegrityViolationException(
		final DataIntegrityViolationException exception,
		final HttpServletRequest request
	) {
		return new ResponseEntity<>(
			buildDetails(exception.getMostSpecificCause().getMessage(), request),
			HttpStatus.BAD_REQUEST
		);
	}

	private Map<String, String> buildDetails(final String message, final HttpServletRequest request) {
		final Map<String, String> details = new HashMap<>();
		details.put("message", message);
		details.put("path", request.getRequestURI());
		return details;
	}
}
