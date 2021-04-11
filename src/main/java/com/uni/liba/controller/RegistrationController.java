package com.uni.liba.controller;

import com.uni.liba.model.UserDto;
import com.uni.liba.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Controller
public class RegistrationController {

	@Autowired
	private UserService userService;

	@GetMapping("/registration")
	public String registration(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "registration";
	}

	@PostMapping("/registration")
	public String addUser(@ModelAttribute("user") @Valid UserDto user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			populateModelWithErrorsMap(bindingResult, model);
			model.addAttribute("user", user);
			return "registration";
		}
		if (isFalse(userService.addUser(user))) {
			model.addAttribute("userExistsError", "User already exists!");
			model.addAttribute("user", user);
			return "registration";
		}
		return "redirect:/login";
	}


	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		httpSession.invalidate();
		return "redirect:/login";
	}


	public static void populateModelWithErrorsMap(BindingResult bindingResult, Model model) {
		model.mergeAttributes(getErrorsMap(bindingResult));
	}

	public static Map<String, String> getErrorsMap(BindingResult bindingResult) {
		return bindingResult.getFieldErrors().stream()
				.collect(Collectors.toMap(fieldError -> fieldError.getField() + "Error", DefaultMessageSourceResolvable::getDefaultMessage));
	}
}
