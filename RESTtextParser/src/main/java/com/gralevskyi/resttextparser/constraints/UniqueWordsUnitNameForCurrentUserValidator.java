package com.gralevskyi.resttextparser.constraints;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gralevskyi.resttextparser.data.WordsRepository;
import com.gralevskyi.resttextparser.domain.TextAndParsedWordsUnit;
import com.gralevskyi.resttextparser.security.JwtUserDetailsService;

@Component
public class UniqueWordsUnitNameForCurrentUserValidator implements ConstraintValidator<UniqueWordsUnitNameForCurrentUser, String> {

	WordsRepository wordsRepo;
	JwtUserDetailsService userService;

	public UniqueWordsUnitNameForCurrentUserValidator() {
		super();
	}

	@Autowired
	public UniqueWordsUnitNameForCurrentUserValidator(WordsRepository wordsRepo, JwtUserDetailsService userService) {
		this.wordsRepo = wordsRepo;
		this.userService = userService;
	}

	@Override
	public boolean isValid(String checkedUnitName, ConstraintValidatorContext context) {
		if (this.wordsRepo != null && this.userService != null) {
			List<TextAndParsedWordsUnit> WordsUnitsOfCurrentUser = wordsRepo.findByUserUsername(userService.getCurrentUsername());
			for (TextAndParsedWordsUnit textAndParsedWordsUnit : WordsUnitsOfCurrentUser) {
				if (textAndParsedWordsUnit.getName().equalsIgnoreCase(checkedUnitName))
					return false;
			}
			return true;
		} else {
			return true;
		}
	}

	@Override
	public void initialize(UniqueWordsUnitNameForCurrentUser constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

}
