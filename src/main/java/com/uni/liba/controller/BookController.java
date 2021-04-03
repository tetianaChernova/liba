package com.uni.liba.controller;

import com.uni.liba.exceptions.BookAlreadyExistsException;
import com.uni.liba.model.Book;
import com.uni.liba.model.BookDto;
import com.uni.liba.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
	public ResponseEntity<Collection<BookDto>> getAllBooks(Authentication authentication) {
		return ResponseEntity.ok(bookService.getAll(authentication.getName()));
	}

	@GetMapping("/search")
	public ResponseEntity<Page<BookDto>> searchBooks(
			Authentication authentication,
			@RequestParam("searchParam") String searchParam,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		return ResponseEntity.ok(bookService.searchBook(authentication.getName(), searchParam, PageRequest.of(page - 1, size)));
	}

	@GetMapping("/{isbn}")
	public ResponseEntity<?> getById(@PathVariable("isbn") String isbn) {
		Optional<Book> book = bookService.getBookById(isbn);
		return book.isPresent()
				? ResponseEntity.ok(book.get())
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}


	@PostMapping("/{isbn}/like")
	public ResponseEntity<?> likeBook(@PathVariable("isbn") String isbn, Authentication authentication) {
		Optional<Book> book = bookService.getBookById(isbn);
		if (book.isPresent()){
			bookService.likeBook(authentication.getName(), isbn);
		}
		return book.isPresent()
				? ResponseEntity.ok().build()
				:ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping("/{isbn}/unlike")
	public ResponseEntity<Void> unlikeBook(@PathVariable("isbn") String isbn, Authentication authentication) {
		bookService.unlikeBook(isbn, authentication.getName());
		return ResponseEntity.ok().build();
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

	@GetMapping("/fav")
	public ResponseEntity<?> favBooks(Authentication authentication) {
		return ResponseEntity.ok(bookService.getAllFavBooks(authentication.getName()));
	}

}
