package com.uni.liba.service;

import com.uni.liba.exceptions.BookAlreadyExistsException;
import com.uni.liba.model.Book;
import com.uni.liba.model.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public interface BookService {

	Book saveBook(final Book bookToSave) throws BookAlreadyExistsException;

	Optional<Book> getBookById(String isbn);

	Collection<BookDto> getAll(String username);

	boolean isBookAlreadyExists(final String isbn, Pageable pageable);

	Page<BookDto> searchBook(String username, String searchParam, Pageable pageable);

	Collection<Book> getAllFavBooks(String username);

	void likeBook(String username, String isbn);

	void unlikeBook(String username, String isbn);
}
