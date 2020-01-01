package com.gralevskyi.resttextparser.security;

import javax.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gralevskyi.resttextparser.data.UserRepository;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	private UserRepository userRepo;
	private PasswordEncoder passwordEncoder;

	public RegistrationController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping
	public String registerForm(Model model) {
		model.addAttribute("registrationForm", new RegistrationForm());
		return "registration";
	}

	@PostMapping
	public String processRegistration(Model model, @Valid RegistrationForm registrationForm, Errors errors) {
		if (errors.hasErrors()) {
			model.addAttribute("registrationForm", registrationForm);
			return "registration";
		}

		userRepo.save(registrationForm.toUser(passwordEncoder));
		return "redirect:/login";
	}

}
