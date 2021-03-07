package com.uni.liba.repo;

import com.uni.liba.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

	Optional<Book> findByIsbn(String isbn);

	List<Book> findByTitleContainingOrAuthorContainingOrIsbnContaining(String titleLike, String authorLike, String isbnLike);

}
