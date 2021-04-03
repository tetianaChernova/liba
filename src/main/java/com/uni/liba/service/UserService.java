package com.uni.liba.service;

import com.uni.liba.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	boolean addUser(User user);
}
