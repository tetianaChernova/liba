package com.uni.liba.service.impl;

import com.uni.liba.model.User;
import com.uni.liba.repo.UserRepository;
import com.uni.liba.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private static final String ROLE_READER = "READER";

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.map(this::buildUserDetails)
				.orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
	}

	@Override
	public boolean addUser(User user) {
		if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			return true;
		} else {
			return false;
		}
	}

	private UserDetails buildUserDetails(User user) {
		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.authorities(ROLE_READER)
				.build();
	}

	@PostConstruct
	private void initDatabase() {
		userRepository.save(User.builder()
				.username("admin")
				.password(passwordEncoder.encode("123"))
				.build()
		);
	}
}
