package com.uni.liba.controller;

import com.uni.liba.model.User;
import com.uni.liba.model.UserDto;
import com.uni.liba.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Controller
public class RegistrationController {

	@Resource
	private UserService userService;

	@GetMapping("/registration")
	public String registration(Model model) {
		UserDto userDto = new UserDto();
		model.addAttribute("user", userDto);
		return "registration";
	}

	@PostMapping("/registration")
	public String addUser(@ModelAttribute("user") @Valid User user) {
		if (isFalse(userService.addUser(user))) {
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
}
