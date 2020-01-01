package com.gralevskyi.resttextparser.web.api;

import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import com.gralevskyi.resttextparser.domain.SavedWordsList;
import com.gralevskyi.resttextparser.domain.User;
import com.gralevskyi.resttextparser.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping(path = "/all", produces = "application/json")
@CrossOrigin(origins = "*")
public class RestCabinetController {
	private WordsRepository wordsRepo;
	private UserRepository userRepo;
	private JwtTokenProvider jwtTokenProvider;

	public RestCabinetController(WordsRepository wordsRepo, JwtTokenProvider jwtTokenProvider, UserRepository userRepo) {
		super();
		this.wordsRepo = wordsRepo;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userRepo = userRepo;

	}

	@GetMapping
	public Iterable<SavedWordsList> returnUserSavedWordsList(@RequestHeader("Authorization") String token) {
		String username = jwtTokenProvider.getUsername(token.substring(7));
		Iterable<SavedWordsList> wordsList = (Iterable<SavedWordsList>) wordsRepo.findByUserUsername(username);
		return wordsList;
	}

	@GetMapping("/{name}")
	public SavedWordsList getByName(@PathVariable("name") String name) {
		SavedWordsList savedList = wordsRepo.findByName(name);
		if (savedList != null) {
			return savedList;
		}
		return null;
	}

	@PutMapping(path = "/{name}", consumes = "application/json")
	public ResponseEntity<Object> putSavedWordsList(@RequestHeader("Authorization") String token, @PathVariable("name") String oldName, @Valid @RequestBody SavedWordsList savedWordsList, Errors errors) {
		if (oldName.equals(savedWordsList.getName())) {
			return new ResponseEntity<>("Please, type new name. You have not change it.", HttpStatus.BAD_REQUEST);
		}
		if (errors.hasErrors()) {
			FieldError fieldError = errors.getFieldError();
			String message = fieldError.getDefaultMessage();
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}

		String username = jwtTokenProvider.getUsername(token.substring(7));
		// if (wordsRepo.findByUserUsernameAndName(username, savedWordsList.getName())
		// != null) {
		// return new ResponseEntity<>("You have already created a list with this name.
		// Please, choose another name.", HttpStatus.CONFLICT);
		// }
		User user = userRepo.findByUsername(username);
		wordsRepo.deleteByUserIdAndName(user.getId(), oldName);
		savedWordsList.setUser(user);
		wordsRepo.save(savedWordsList);
		return new ResponseEntity<>("", HttpStatus.CREATED);
	}

	@PatchMapping(path = "/{name}", consumes = "application/json")
	public ResponseEntity<Object> patchSavedWordsList(@RequestHeader("Authorization") String token, @PathVariable("name") String oldName, @RequestBody String newName) {
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
		wordsRepo.updateWordsListNameByUser(newName, oldName, user);
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	@DeleteMapping(path = "/{name}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Transactional
	public void deleteSavedWordsList(@PathVariable("name") String name) {
		try {
			wordsRepo.deleteByName(name);
		} catch (EmptyResultDataAccessException e) {

		}
	}

}