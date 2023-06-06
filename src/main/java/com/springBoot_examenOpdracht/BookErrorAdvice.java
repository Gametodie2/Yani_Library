package com.springBoot_examenOpdracht;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import exceptions.AuthorNotFoundException;

@RestControllerAdvice
public class BookErrorAdvice {
	@ResponseBody 
	@ExceptionHandler(AuthorNotFoundException.class)
	String authorNotFoundHandler(AuthorNotFoundException ex) {
		return ex.getMessage();
	}
}
