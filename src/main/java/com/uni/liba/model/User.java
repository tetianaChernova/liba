package com.uni.liba.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	private String username;
	private String password;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "user_book",
			joinColumns = {@JoinColumn(name = "username")},
			inverseJoinColumns = {@JoinColumn(name = "isbn")})
	private List<Book> likedBooks;
}
