package com.uni.liba.exceptions;

public class BookAlreadyExistsException extends Exception {

	public BookAlreadyExistsException() {
	}

	public BookAlreadyExistsException(String message) {
		super(message);
	}
}
