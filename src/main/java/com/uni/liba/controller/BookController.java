package com.uni.liba.controller;

import com.uni.liba.exceptions.BookAlreadyExistsException;
import com.uni.liba.model.Book;
import com.uni.liba.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("rest/books")
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

	@GetMapping("/{isbn}")
	public ResponseEntity<?> getById(@PathVariable("isbn") String isbn) {
		Optional<Book> book = bookService.getBookById(isbn);
		return book.isPresent()
				? ResponseEntity.ok(book.get())
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping
	public ResponseEntity<?> addNewBook(@RequestBody Book book) {
		Book savedBook;
		try {
			savedBook = bookService.saveBook(book);
		} catch (BookAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.ok(savedBook);
	}

}
