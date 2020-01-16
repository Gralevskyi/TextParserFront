package com.gralevskyi.resttextparser.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gralevskyi.resttextparser.data.UserRepository;
import com.gralevskyi.resttextparser.domain.user.Role;
import com.gralevskyi.resttextparser.domain.user.User;
import com.gralevskyi.resttextparser.domain.user.UserStatus;
import com.gralevskyi.resttextparser.security.AuthenticationRequestDto;
import com.gralevskyi.resttextparser.security.RegistrationForm;
import com.gralevskyi.resttextparser.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping(path = "/user", produces = "application/json")
@CrossOrigin(origins = "*")
public class UserController {
	private UserRepository userRepo;
	private PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;

	public UserController(UserRepository userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping(path = "/register", consumes = "application/json")
	public ResponseEntity<Object> registerNewUser(@Valid @RequestBody RegistrationForm registrationForm, Errors errors) {

		if (errors.hasErrors()) {
			Map<String, String> responseErrors = new HashMap<String, String>();
			for (FieldError fieldError : errors.getFieldErrors()) {
				String fieldName = fieldError.getField();
				String message = fieldError.getDefaultMessage();
				responseErrors.put(fieldName, message);
			}

			return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
		}
		User user = registrationForm.toUser(passwordEncoder);
		Role role = new Role();
		role.setName("USER");
		role.setId((long) 1);
		List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		user.setRoles(roles);
		user.setStatus(UserStatus.ACTIVE);
		userRepo.save(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody AuthenticationRequestDto authRequestDto) {
		try {
			String username = authRequestDto.getUsername();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authRequestDto.getPassword()));
			User user = userRepo.findByUsername(username);
			if (user == null) {
				throw new UsernameNotFoundException("User with username" + username + "not found");
			}

			String token = jwtTokenProvider.createToken(username, user.getRoles());
			Date expiresAt = jwtTokenProvider.getExpirationDateFromToken(token);

			Map<Object, Object> response = new HashMap<Object, Object>();
			response.put("username", username);
			response.put("token", token);
			response.put("expiresAt", expiresAt);

			return ResponseEntity.ok(response);
		} catch (AuthenticationException e) {
			return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
		}

	}
}
