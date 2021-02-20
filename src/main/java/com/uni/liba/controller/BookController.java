package com.uni.liba.controller;

import com.uni.liba.exceptions.BookAlreadyExistsException;
import com.uni.liba.model.Book;
import com.uni.liba.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping
	public ResponseEntity<Collection<Book>> getAllBooks() {
		return ResponseEntity.ok(bookService.getAll());
	}

	@GetMapping("/search")
	public ResponseEntity<Collection<Book>> searchBooks(@RequestParam("searchParam") String searchParam) {
		return ResponseEntity.ok(bookService.searchBook(searchParam));
	}

	@PostMapping("/save")
	public ResponseEntity<?> addNewBook(@RequestBody Book book) {
		try {
			bookService.saveBook(book);
		} catch (BookAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.ok(bookService.getAll());
	}

}
