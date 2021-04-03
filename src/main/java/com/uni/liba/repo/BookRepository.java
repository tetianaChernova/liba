package com.uni.liba.repo;

import com.uni.liba.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String>, JpaSpecificationExecutor<Book> {

	Optional<Book> findByIsbn(String isbn);

	@Query(value = "Select * from book inner join user_book ub on ub.isbn = book.isbn where ub.username = :username", nativeQuery = true)
	Collection<Book> findAllFavBooks(@Param("username") String username);

	@Query(value = "insert into user_book(username, isbn) values(:username, :isbn)", nativeQuery = true)
	void likeBook(String username, String isbn);

	@Query(value = "delete from user_book where user_book.isbn = :isbn and user_book.username = :username", nativeQuery = true)
	void unlikeBook(String username, String isbn);
}
