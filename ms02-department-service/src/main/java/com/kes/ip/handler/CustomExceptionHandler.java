package com.kes.ip.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.kes.ip.dto.ErrorMessage;
import com.kes.ip.exception.DepartmentServiceBusinessException;
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

	@ExceptionHandler(value = DepartmentServiceBusinessException.class)
	public ResponseEntity<ErrorMessage> handleDepartmentServiceBusinessException(
			DepartmentServiceBusinessException exception, WebRequest webRequest) {

		ErrorMessage errorMessage = ErrorMessage.builder().message(exception.getMessage())
				.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.timeStamp(LocalDateTime.now().toString()).path(webRequest.getDescription(false)).build();

		return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
