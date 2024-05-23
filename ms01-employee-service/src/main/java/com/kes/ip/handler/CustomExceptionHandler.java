package com.kes.ip.handler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.kes.ip.dto.ErrorMessage;
import com.kes.ip.exception.EmployeeServiceBusinessException;
import com.kes.ip.exception.ResourceNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest webRequest) {

		ErrorMessage errorMessage = ErrorMessage.builder().message(exception.getMessage())
				.httpStatus(HttpStatus.BAD_REQUEST).statusCode(HttpStatus.BAD_REQUEST.value())
				.timeStamp(LocalDateTime.now().toString()).path(webRequest.getDescription(false)).build();

		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EmployeeServiceBusinessException.class)
	public ResponseEntity<ErrorMessage> handleEmployeeServiceBusinessException(
			EmployeeServiceBusinessException exception, WebRequest webRequest) {

		ErrorMessage errorMessage = ErrorMessage.builder().message(exception.getMessage())
				.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.timeStamp(LocalDateTime.now().toString()).path(webRequest.getDescription(false)).build();

		return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
			WebRequest webRequest) {

		Map<String, String> errorsMap = new ConcurrentHashMap<>();

		exception.getBindingResult().getAllErrors().forEach(error -> {

			String message = error.getDefaultMessage();

			String field = ((FieldError) error).getField();

			errorsMap.put(message, field);
		});

		return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
	}
}
