package com.uni.liba.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private static final String USERNAME_REGEXP = "[A-Za-z0-9]+$";
	private static final String INVALID_USERNAME_ERROR_MESSAGE = "Invalid username: only letters are allowed!!!";
	private static final String INVALID_PASSWORD_ERROR_MESSAGE = "Minimum length of password is 8 and Maximum is 20!!!";

	@NotNull
	@NotEmpty
	@Pattern(regexp = USERNAME_REGEXP, message = INVALID_USERNAME_ERROR_MESSAGE)
	private String username;
	@NotNull
	@NotEmpty
	@Size(min = 8, max = 20, message = INVALID_PASSWORD_ERROR_MESSAGE)
	private String password;

}
