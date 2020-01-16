package com.gralevskyi.resttextparser.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gralevskyi.resttextparser.security.JwtUserDetailsService;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

	JwtUserDetailsService userService;

	@Autowired
	public UniqueUsernameValidator(JwtUserDetailsService userService) {
		this.userService = userService;
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		return !userService.isUsernameAlreadyExist(username);

	}

}
