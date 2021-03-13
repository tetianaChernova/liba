package com.uni.liba.service;

import com.uni.liba.exceptions.BookAlreadyExistsException;
import com.uni.liba.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public interface BookService {

	Book saveBook(final Book bookToSave) throws BookAlreadyExistsException;

	Optional<Book> getBookById(String isbn);

	Collection<Book> getAll();

	boolean isBookAlreadyExists(final String isbn, Pageable pageable);

	Page<Book> searchBook(String searchParam, Pageable pageable);
}
