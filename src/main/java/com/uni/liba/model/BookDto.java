package com.uni.liba.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDto {
	private String isbn;
	private String title;
	private String author;
	private boolean meliked;

	public BookDto(Book book, boolean meliked) {
		this.isbn = book.getIsbn();
		this.title = book.getTitle();
		this.author = book.getAuthor();
		this.meliked = meliked;
	}
}
