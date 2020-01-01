package com.gralevskyi.resttextparser.constraints;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gralevskyi.resttextparser.data.WordsRepository;
import com.gralevskyi.resttextparser.domain.SavedWordsList;
import com.gralevskyi.resttextparser.security.JwtUserDetailsService;

@Component
public class UniqueWordsListNameForCurrentUserValidator implements ConstraintValidator<UniqueWordsListNameForCurrentUser, String> {

	WordsRepository wordsRepo;
	JwtUserDetailsService userService;

	public UniqueWordsListNameForCurrentUserValidator() {
		super();
	}

	@Autowired
	public UniqueWordsListNameForCurrentUserValidator(WordsRepository wordsRepo, JwtUserDetailsService userService) {
		super();
		this.wordsRepo = wordsRepo;
		this.userService = userService;
	}

	@Override
	public boolean isValid(String listName, ConstraintValidatorContext context) {
		if (this.wordsRepo != null && this.userService != null) {
			List<SavedWordsList> WordsListsOfCurrentUser = wordsRepo.findByUserUsername(userService.getCurrentUsername());
			for (SavedWordsList SavedWordsList : WordsListsOfCurrentUser) {
				if (SavedWordsList.getName().equalsIgnoreCase(listName))
					return false;
			}
			return true;
		} else {
			return true;
		}
	}

	@Override
	public void initialize(UniqueWordsListNameForCurrentUser constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

}
