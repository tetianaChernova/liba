package com.uni.liba.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.liba.exceptions.BookAlreadyExistsException;
import com.uni.liba.model.Book;
import com.uni.liba.model.BookDto;
import com.uni.liba.service.BookService;
import com.uni.liba.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(BookController.class)
@WithMockUser(username = "tetiana")
class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private BookService bookService;
	@MockBean
	private UserService userService;

	private BookDto testBook1;
	private BookDto testBook2;
	private List<BookDto> bookFromDB;

	@BeforeEach
	void setUp() throws BookAlreadyExistsException {
		testBook1 = BookDto.builder().isbn("testIsbn1").title("testTitle1").author("testAuthor1").build();
		testBook2 = BookDto.builder().isbn("9781566199094").title("testTitle2").author("testAuthor2").build();
		bookFromDB = List.of(
				BookDto.builder().isbn("isbn1").title("title1").author("author1").build(),
				BookDto.builder().isbn("isbn2").title("title2").author("author2").build(),
				BookDto.builder().isbn("isbn3").title("title3").author("author3").build());
		PageRequest pageRequest = PageRequest.of(0, 20);
		when(bookService.getAll("tetiana")).thenReturn(bookFromDB);
		when(bookService.searchBook("tetiana", "title1", pageRequest))
				.thenReturn(new PageImpl<>(List.of(testBook1)));
		when(bookService.saveBook(testBook2)).thenReturn(Book.builder()
				.isbn(testBook2.getIsbn())
				.title(testBook2.getTitle())
				.author(testBook2.getAuthor())
				.build());
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
				.param("searchParam", "title1")
				.param("page", "1")
				.param("size", "20"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
						new PageImpl<>(List.of(testBook1)))));
	}

	@Test
	void shouldSuccessfullySaveAndReturnSavedBook() throws Exception {
		mockMvc.perform(
				post("/rest/books")
						.content(objectMapper.writeValueAsString(testBook2))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(Book.builder()
						.isbn(testBook2.getIsbn())
						.title(testBook2.getTitle())
						.author(testBook2.getAuthor())
						.build())));
	}
}
