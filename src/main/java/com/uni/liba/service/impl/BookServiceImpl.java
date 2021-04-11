package com.uni.liba.service.impl;

import com.uni.liba.exceptions.BookAlreadyExistsException;
import com.uni.liba.model.Book;
import com.uni.liba.model.BookDto;
import com.uni.liba.model.Book_;
import com.uni.liba.model.User;
import com.uni.liba.repo.BookRepository;
import com.uni.liba.repo.UserRepository;
import com.uni.liba.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private UserRepository userRepository;

	public Book saveBook(final BookDto bookToSave) throws BookAlreadyExistsException {
		if (getBookById(bookToSave.getIsbn()).isPresent()) {
			throw new BookAlreadyExistsException(bookToSave.getIsbn());
		}
		Book book = Book.builder()
				.isbn(bookToSave.getIsbn())
				.title(bookToSave.getTitle())
				.author(bookToSave.getAuthor())
				.build();
		return bookRepository.save(book);
	}

	public Optional<BookDto> getBookById(String isbn) {
		Optional<Book> book = bookRepository.findByIsbn(isbn);
		return book.map(value -> BookDto.builder().isbn(value.getIsbn()).title(value.getTitle()).author(value.getAuthor()).build());
	}

	public Collection<BookDto> getAll(String username) {
		Collection<Book> favBooks = getFavouriteByUsername(username);
		return bookRepository.findAll().stream().map(book -> new BookDto(book.getIsbn(), book.getAuthor(), book.getTitle(), favBooks.contains(book))).collect(toList());
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
		Collection<Book> favBooks = getFavouriteByUsername(username);

		return bookRepository.findAll(bookSpec, pageable).map(book -> new BookDto(book.getIsbn(), book.getAuthor(), book.getTitle(), favBooks.contains(book)));

	}

	@Override
	public List<Book> getFavouriteByUsername(String username) {
		return getUserByUsername(username).getLikedBooks();
	}

	@Override
	public void likeBook(String username, String isbn) {
		User userEntity = getUserByUsername(username);
		userEntity.getLikedBooks().add(bookRepository.getOne(isbn));
	}

	@Override
	public void unlikeBook(String username, String isbn) {
		User userEntity = getUserByUsername(username);
		userEntity.getLikedBooks().removeIf(book -> book.getIsbn().equals(isbn));
	}


	private User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Can't find user with such username:" + username));
	}
}
