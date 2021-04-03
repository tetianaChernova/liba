package com.uni.liba.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.liba.exceptions.BookAlreadyExistsException;
import com.uni.liba.model.Book;
import com.uni.liba.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(BookController.class)
class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private BookService bookService;

	private Book testBook1;
	private Book testBook2;
	private List<Book> bookFromDB;

	@BeforeEach
	void setUp() throws BookAlreadyExistsException {
		testBook1 = Book.builder().isbn("testIsbn1").title("testTitle1").author("testAuthor1").build();
		testBook2 = Book.builder().isbn("testIsbn2").title("testTitle2").author("testAuthor2").build();
		bookFromDB = List.of(
				Book.builder().isbn("isbn1").title("title1").author("author1").build(),
				Book.builder().isbn("isbn2").title("title2").author("author2").build(),
				Book.builder().isbn("isbn3").title("title3").author("author3").build());
//		when(bookService.getAll()).thenReturn(bookFromDB);
//		when(bookService.searchBook("testIsbn1", PageRequest.of(0, 10)))
//				.thenReturn(new PageImpl<>(Collections.singletonList(testBook1)));
		when(bookService.saveBook(testBook2)).thenReturn(testBook2);
	}

	@Test
	void shouldSuccessfullyReturnAllBooks() throws Exception {
		mockMvc.perform(get("/rest/books"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(bookFromDB)));
	}

	@Test
	void shouldSuccessfullyReturnBooksBySearchParam() throws Exception {
		mockMvc.perform(get("/rest/books/search")
				.param("searchParam", "testIsbn1")
				.param("page", "1")
				.param("size", "10"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
						new PageImpl<>(Collections.singletonList(testBook1)))));
	}

	@Test
	void shouldSuccessfullySaveAndReturnSavedBook() throws Exception {
		mockMvc.perform(
				post("/rest/books")
						.content(objectMapper.writeValueAsString(testBook2))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(testBook2)));
	}
}
