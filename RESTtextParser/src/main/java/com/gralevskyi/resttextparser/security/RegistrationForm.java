package com.gralevskyi.resttextparser.security;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.gralevskyi.resttextparser.constraints.PasswordConfirmationCheck;
import com.gralevskyi.resttextparser.constraints.UniqueUsername;
import com.gralevskyi.resttextparser.domain.user.User;

@PasswordConfirmationCheck(first = "passwordConfirmation", second = "password")
public class RegistrationForm {
	@NotBlank(message = "Name is required.")
	@UniqueUsername
	private String username;

	@NotNull(message = "Password must be at least 5 but not longer then 25 characters.")
	@Size(min = 5, max = 25, message = "Password must be at least 5 but not longer then 25 characters.")
	private String password;
	private String passwordConfirmation;
	private String fullname;

	@Email(message = "Email must be valid.")
	private String email;
	private String phone;

	public User toUser(PasswordEncoder passwordEncoder) {
		return new User(username, passwordEncoder.encode(password), fullname, email, phone);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
