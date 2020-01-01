package com.gralevskyi.resttextparser.web.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gralevskyi.resttextparser.data.UserRepository;
import com.gralevskyi.resttextparser.domain.Role;
import com.gralevskyi.resttextparser.domain.Status;
import com.gralevskyi.resttextparser.domain.User;
import com.gralevskyi.resttextparser.security.RegistrationForm;

@RestController
@RequestMapping(path = "/api/", produces = "application/json")
@CrossOrigin(origins = "*")
public class RestUserController {
	private UserRepository userRepo;
	private PasswordEncoder passwordEncoder;

	public RestUserController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.userRepo = userRepo;
	}

	@PostMapping(path = "/registr", consumes = "application/json")
	public ResponseEntity<Object> processRegistration(@Valid @RequestBody RegistrationForm registrationForm, Errors errors) {

		if (errors.hasErrors()) {
			Map<String, String> responseErrors = new HashMap<String, String>();
			for (FieldError fieldError : errors.getFieldErrors()) {
				String fieldName = fieldError.getField();
				String message = fieldError.getDefaultMessage();
				responseErrors.put(fieldName, message);
			}

			return new ResponseEntity<>(responseErrors, HttpStatus.OK);
		}
		User user = registrationForm.toUser(passwordEncoder);
		Role role = new Role();
		role.setName("USER");
		role.setId((long) 1);
		List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		user.setRoles(roles);
		user.setStatus(Status.ACTIVE);
		userRepo.save(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
