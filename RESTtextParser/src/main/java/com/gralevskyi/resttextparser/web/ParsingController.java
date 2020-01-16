package com.gralevskyi.resttextparser.web;

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
import com.gralevskyi.resttextparser.domain.TextAndParsedWordsUnit;
import com.gralevskyi.resttextparser.domain.TextForParsing;
import com.gralevskyi.resttextparser.domain.TextParser;
import com.gralevskyi.resttextparser.domain.user.User;
import com.gralevskyi.resttextparser.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping(path = "/parse", produces = "application/json")
@CrossOrigin(origins = "*")
public class ParsingController {

	@Autowired
	TextParser textParser;

	WordsRepository wordsRepo;
	UserRepository userRepo;
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	ParsingController(WordsRepository wordsRepo, UserRepository userRepo, JwtTokenProvider jwtTokenProvider) {
		this.wordsRepo = wordsRepo;
		this.userRepo = userRepo;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping(consumes = "application/json")
	public ResponseEntity<Object> parseText(@Valid @RequestBody TextForParsing textForParsing, Errors errors) {
		if (errors.hasErrors()) {
			FieldError fieldError = errors.getFieldError();
			String errorMessage = fieldError.getDefaultMessage();
			return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		}
		Map<String, Integer> output = textParser.parse(textForParsing.getText());
		return new ResponseEntity<>(output, HttpStatus.OK);
	}

	@PostMapping(path = "/saveParsedWords", consumes = "application/json")
	public ResponseEntity<Object> saveNewTextAndWordsUnit(@Valid @RequestBody TextAndParsedWordsUnit newTextAndParsedWordsUnit, Errors errors, @RequestHeader("Authorization") String token) {
		if (errors.hasErrors()) {
			FieldError fieldError = errors.getFieldError();
			String errorMessage = fieldError.getDefaultMessage();
			return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		} else {
			String username = jwtTokenProvider.getUsername(token.substring(7));
			User user = userRepo.findByUsername(username);
			newTextAndParsedWordsUnit.setUser(user);
			wordsRepo.save(newTextAndParsedWordsUnit);
			return new ResponseEntity<>(newTextAndParsedWordsUnit, HttpStatus.CREATED);
		}

	}

}
