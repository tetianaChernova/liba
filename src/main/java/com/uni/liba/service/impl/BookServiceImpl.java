package com.uni.liba.service.impl;

import com.uni.liba.exceptions.BookAlreadyExistsException;
import com.uni.liba.model.Book;
import com.uni.liba.repo.BookRepository;
import com.uni.liba.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	public Book saveBook(final Book bookToSave) throws BookAlreadyExistsException {
		if (getBookById(bookToSave.getIsbn()).isPresent()) {
			throw new BookAlreadyExistsException(bookToSave.getIsbn());
		}
		return bookRepository.saveBook(bookToSave);
	}

	public Optional<Book> getBookById(String isbn) {
		return bookRepository.getBookById(isbn);
	}

	public Collection<Book> getAll() {
		return bookRepository.getAll();
	}

	public boolean isBookAlreadyExists(final String isbn) {
		return bookRepository.isBookAlreadyExists(isbn);
	}

	@Override
	public Collection<Book> searchBook(String searchParam) {
		return bookRepository.searchBook(searchParam);
	}
}
