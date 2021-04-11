package com.uni.liba.controller;


import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;


@ControllerAdvice

public class ExceptionsHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException exception) {
		return ResponseEntity
				.badRequest()
				.body(exception.getBindingResult().getFieldErrors()
						.stream()
						.collect(Collectors.toMap(fieldError -> fieldError.getField() + "Error",
								DefaultMessageSourceResolvable::getDefaultMessage)));
	}
}

