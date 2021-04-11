package com.uni.liba.service;

import com.uni.liba.model.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	boolean addUser(UserDto user);
}
