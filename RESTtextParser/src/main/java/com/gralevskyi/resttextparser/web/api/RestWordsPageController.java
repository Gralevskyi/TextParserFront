package com.gralevskyi.resttextparser.web.api;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gralevskyi.resttextparser.data.UserRepository;
import com.gralevskyi.resttextparser.data.WordsRepository;
import com.gralevskyi.resttextparser.domain.SavedWordsList;
import com.gralevskyi.resttextparser.domain.TextDto;
import com.gralevskyi.resttextparser.domain.TextParser;
import com.gralevskyi.resttextparser.domain.User;
import com.gralevskyi.resttextparser.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping(path = "/parse", produces = "application/json")
@CrossOrigin(origins = "*")
public class RestWordsPageController {

	@Autowired
	TextParser textParser;

	WordsRepository wordsRepo;
	UserRepository userRepo;
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	RestWordsPageController(WordsRepository wordsRepo, UserRepository userRepo, JwtTokenProvider jwtTokenProvider) {
		this.wordsRepo = wordsRepo;
		this.userRepo = userRepo;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<Object> showWordsPage(@Valid @RequestBody TextDto text, Errors errors) {
		if (errors.hasErrors()) {
			// Map<String, String> responseError = new HashMap<String, String>();
			FieldError fieldError = errors.getFieldError();
			String message = fieldError.getDefaultMessage();
			// responseError.put("textSize", message);
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
		Map<String, Integer> output = textParser.parse(text.getText());
		return new ResponseEntity<>(output, HttpStatus.OK);
	}

	@PostMapping(path = "/save", consumes = "application/json")
	public ResponseEntity<Object> saveNewWordsList(@Valid @RequestBody SavedWordsList newWordsList, Errors errors, @RequestHeader("Authorization") String token) {
		if (errors.hasErrors()) {
			/*
			 * Map<String, String> responseErrors = new HashMap<String, String>(); for
			 * (FieldError fieldError : errors.getFieldErrors()) { String fieldName =
			 * fieldError.getField(); String message = fieldError.getDefaultMessage();
			 * responseErrors.put(fieldName, message); }
			 */
			FieldError fieldError = errors.getFieldError();
			String message = fieldError.getDefaultMessage();

			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

		} else {
			String username = jwtTokenProvider.getUsername(token.substring(7));
			User user = userRepo.findByUsername(username);
			newWordsList.setUser(user);
			wordsRepo.save(newWordsList);
			return new ResponseEntity<>(newWordsList, HttpStatus.CREATED);
		}

	}

}
