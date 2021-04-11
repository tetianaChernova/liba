package com.uni.liba.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
	@ISBN
	private String isbn;
	@NotBlank
	private String title;
	@NotBlank
	private String author;
	private boolean meliked;

	public BookDto(String isbn, String title, String author) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.meliked = false;
	}

	public BookDto(Book book, boolean meliked) {
		this.isbn = book.getIsbn();
		this.title = book.getTitle();
		this.author = book.getAuthor();
		this.meliked = meliked;
	}
}
