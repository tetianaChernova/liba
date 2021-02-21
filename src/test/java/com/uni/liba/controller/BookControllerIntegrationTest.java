package com.uni.liba.controller;

import com.uni.liba.model.Book;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerIntegrationTest {
	private Book testBook;

	@LocalServerPort
	void savePort(int port) {
		RestAssured.port = port;
	}

	@BeforeEach
	void setUp(){
		testBook = Book.builder().isbn("isbn").title("title").author("author").build();
	}

	@Test
	void shouldSuccessfullySaveAndReturnSavedBook() {
		RestAssured
				.given()
				.body(testBook)
				.contentType(ContentType.JSON)
				.when()
				.post("/books/")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("isbn", CoreMatchers.is(testBook.getIsbn()))
				.body("title", CoreMatchers.is(testBook.getTitle()))
				.body("author", CoreMatchers.is(testBook.getAuthor()));
	}

	@Test
	void shouldSuccessfullyReturnAllBooks() {
		RestAssured
				.given()
				.contentType(ContentType.JSON)
				.when()
				.get("/books")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("", hasItems(hasEntry("isbn", "isbn1"), hasEntry("title", "title1"), hasEntry("author", "author1")), "",
						hasItems(hasEntry("isbn", "isbn2"), hasEntry("title", "title2"), hasEntry("author", "author2")), "",
						hasItems(hasEntry("isbn", "isbn3"), hasEntry("title", "title3"), hasEntry("author", "author3")));
	}

	@Test
	void shouldSuccessfullyReturnBooksBySearchParam() {
		RestAssured
				.given()
				.queryParam("searchParam", "title1")
				.contentType(ContentType.JSON)
				.when()
				.get("books/search")
				.then()
				.statusCode(200)
				.body("size()", is(1));
	}

	@Test
	void shouldReturnEmptyListOfBooksBySearchParam() {
		RestAssured
				.given()
				.queryParam("searchParam", "none")
				.contentType(ContentType.JSON)
				.when()
				.get("books/search")
				.then()
				.statusCode(200)
				.body("size()", is(0));
	}
}
