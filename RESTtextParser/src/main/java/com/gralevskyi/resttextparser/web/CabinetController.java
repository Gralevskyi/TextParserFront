package com.gralevskyi.resttextparser.web;

import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gralevskyi.resttextparser.data.UserRepository;
import com.gralevskyi.resttextparser.data.WordsRepository;
import com.gralevskyi.resttextparser.domain.TextAndParsedWordsUnit;
import com.gralevskyi.resttextparser.domain.user.User;
import com.gralevskyi.resttextparser.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping(path = "/cabinet", produces = "application/json")
@CrossOrigin(origins = "*")
public class CabinetController {
	private WordsRepository wordsRepo;
	private UserRepository userRepo;
	private JwtTokenProvider jwtTokenProvider;

	public CabinetController(WordsRepository wordsRepo, JwtTokenProvider jwtTokenProvider, UserRepository userRepo) {
		super();
		this.wordsRepo = wordsRepo;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userRepo = userRepo;

	}

	@GetMapping(path = "/all")
	public Iterable<TextAndParsedWordsUnit> returnUserSavedWordsList(@RequestHeader("Authorization") String token) {
		String username = jwtTokenProvider.getUsername(token.substring(7));
		Iterable<TextAndParsedWordsUnit> allUserTextAndParsedWordsUnits = (Iterable<TextAndParsedWordsUnit>) wordsRepo.findByUserUsername(username);
		return allUserTextAndParsedWordsUnits;
	}

	@GetMapping(path = "all/{name}")
	public ResponseEntity<Object> getByName(@PathVariable("name") String name, @RequestHeader("Authorization") String token) {
		String username = jwtTokenProvider.getUsername(token.substring(7));
		TextAndParsedWordsUnit textAndParsedWordsUnit = wordsRepo.findByUserUsernameAndName(username, name);
		if (textAndParsedWordsUnit == null) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(textAndParsedWordsUnit, HttpStatus.OK);
	}

	@PutMapping(path = "/{name}", consumes = "application/json")
	public ResponseEntity<Object> replaceOldUnitByNewOne(@RequestHeader("Authorization") String token, @PathVariable("name") String oldName, @Valid @RequestBody TextAndParsedWordsUnit newTextAndParsedWordsUnit, Errors errors) {
		if (errors.hasErrors()) {
			FieldError fieldError = errors.getFieldError();
			String errorMessage = fieldError.getDefaultMessage();
			// in this case error of created unit with the same name must be ignored
			if (!errorMessage.contentEquals("You have already created words unit with this name. Please, choose another name."))
				return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		}
		String username = jwtTokenProvider.getUsername(token.substring(7));
		wordsRepo.deleteByUserUsernameAndName(username, oldName);
		User user = userRepo.findByUsername(username);
		newTextAndParsedWordsUnit.setUser(user);
		wordsRepo.save(newTextAndParsedWordsUnit);
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	@PatchMapping(path = "/{name}", consumes = "application/json")
	public ResponseEntity<Object> changeNameOfTextAndParsedWordsUnit(@RequestHeader("Authorization") String token, @PathVariable("name") String oldName, @RequestBody String newName) {
		if (oldName.equals(newName)) {
			return new ResponseEntity<>("Please, type new name. You have not change it.", HttpStatus.BAD_REQUEST);
		}
		if (newName.length() < 5) {
			return new ResponseEntity<>("Name must be at least 5 character long.", HttpStatus.BAD_REQUEST);
		}
		String username = jwtTokenProvider.getUsername(token.substring(7));

		if (wordsRepo.findByUserUsernameAndName(username, newName) != null) {
			return new ResponseEntity<>("You have already created a list with this name. Please, choose another name.", HttpStatus.CONFLICT);
		}
		User user = userRepo.findByUsername(username);
		wordsRepo.updateWordsUnitNameByUser(newName, oldName, user);
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	@DeleteMapping(path = "/{name}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTextAndParsedWordsUnit(@PathVariable("name") String unitForDeleteName, @RequestHeader("Authorization") String token) {
		String username = jwtTokenProvider.getUsername(token.substring(7));
		try {
			wordsRepo.deleteByUserUsernameAndName(username, unitForDeleteName);
		} catch (EmptyResultDataAccessException e) {

		}
	}

}