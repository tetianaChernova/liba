package com.uni.liba.controller;

import com.uni.liba.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MvcBookController {

	private final BookService bookService;

	@GetMapping()
	public String indexPage() {
		return "index";
	}

	@GetMapping(value = "books/{isbn}")
	public String bookPage(@PathVariable String isbn, Model model) {
		model.addAttribute("book", bookService.getBookById(isbn).get());
		return "bookPage";
	}

	@GetMapping("/books/fav")
	public String favourites(Authentication authentication, Model model) {
		model.addAttribute("books", bookService.getFavouriteByUsername(authentication.getName()));
		return "favourites";
	}
}
