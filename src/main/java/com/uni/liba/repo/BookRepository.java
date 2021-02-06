package com.uni.liba.repo;

import com.uni.liba.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class BookRepository {
	private static final Map<String, Book> BOOK_DATABASE = initDatabase();

	public Book saveBook(final Book bookToSave) {
		final Book book = Book.builder()
				.isbn(bookToSave.getIsbn())
				.title(bookToSave.getTitle())
				.author(bookToSave.getAuthor())
				.build();
		BOOK_DATABASE.put(book.getIsbn(), book);
		log.info("Saved new book: {}", book);
		return book;
	}

	public Optional<Book> getBookById(String isbn) {
		return Optional.ofNullable(BOOK_DATABASE.get(isbn));
	}

	public Collection<Book> getAll() {
		return BOOK_DATABASE.values();
	}

	public boolean isBookAlreadyExists(final String isbn) {
		return BOOK_DATABASE.containsKey(isbn);
	}

	private static Map<String, Book> initDatabase() {
		final Map<String, Book> database = new HashMap<>();
		database.put("isbn1", Book.builder().isbn("isbn1").title("title1").author("author1").build());
		database.put("isbn2", Book.builder().isbn("isbn2").title("title2").author("author2").build());
		database.put("isbn3", Book.builder().isbn("isbn3").title("title3").author("author3").build());
		return database;
	}
}
