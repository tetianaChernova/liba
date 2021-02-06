package com.uni.liba.controller;

import com.uni.liba.exceptions.BookAlreadyExistsException;
import com.uni.liba.model.Book;
import com.uni.liba.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping
	public String getAllBooks(Model model) {
		Collection<Book> books = bookService.getAll();
		model.addAttribute("books", books);
		return "books";
	}

	@GetMapping("/save")
	public String addNewBook() {
		return "bookForm";
	}

	@PostMapping("/save")
	public String addNewBook(@ModelAttribute Book book) {
		try {
			bookService.saveBook(book);
		} catch (BookAlreadyExistsException e) {
			e.printStackTrace();
		}
		return "redirect:/books";
	}

}
