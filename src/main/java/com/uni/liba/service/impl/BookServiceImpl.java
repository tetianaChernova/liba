package com.uni.liba.service.impl;

import com.uni.liba.exceptions.BookAlreadyExistsException;
import com.uni.liba.model.Book;
import com.uni.liba.model.BookDto;
import com.uni.liba.model.Book_;
import com.uni.liba.repo.BookRepository;
import com.uni.liba.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	public Book saveBook(final Book bookToSave) throws BookAlreadyExistsException {
		if (getBookById(bookToSave.getIsbn()).isPresent()) {
			throw new BookAlreadyExistsException(bookToSave.getIsbn());
		}
		return bookRepository.save(bookToSave);
	}

	public Optional<Book> getBookById(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	public Collection<BookDto> getAll(String username) {
		Collection<Book> favBooks = getAllFavBooks(username);
		return bookRepository.findAll().stream().map(book -> new BookDto(book.getIsbn(), book.getAuthor(), book.getTitle(), favBooks.contains(book))).collect(Collectors.toList());
	}

	@Override
	public boolean isBookAlreadyExists(String isbn, Pageable pageable) {
		return bookRepository.findByIsbn(isbn).isPresent();
	}

	@Override
	public Page<BookDto> searchBook(String username, String searchParam, Pageable pageable) {
		Specification<Book> bookSpec = (root, query, criteriaBuilder) -> criteriaBuilder.or(
				criteriaBuilder.like(root.get(Book_.ISBN), "%" + searchParam + "%"),
				criteriaBuilder.like(root.get(Book_.AUTHOR), "%" + searchParam + "%"),
				criteriaBuilder.like(root.get(Book_.TITLE), "%" + searchParam + "%")
		);
		Collection<Book> favBooks = getAllFavBooks(username);

		return bookRepository.findAll(bookSpec, pageable).map(book -> new BookDto(book.getIsbn(), book.getAuthor(), book.getTitle(), favBooks.contains(book)));

	}

	@Override
	public Collection<Book> getAllFavBooks(String username) {
		return bookRepository.findAllFavBooks(username);
	}

	@Override
	public void likeBook(String username, String isbn) {
		bookRepository.likeBook(username, isbn);
	}

	@Override
	public void unlikeBook(String username, String isbn) {
		bookRepository.unlikeBook(username, isbn);
	}
}
