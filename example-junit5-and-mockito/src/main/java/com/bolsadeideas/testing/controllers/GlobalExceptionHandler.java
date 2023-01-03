package com.bolsadeideas.testing.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		
		String errorMessage = "";
		
		for (ObjectError objectError : e.getAllErrors()) {
			errorMessage += objectError.getDefaultMessage() + "; ";
		}
		
		return ResponseEntity.badRequest().body(errorMessage);
	}
	

}
