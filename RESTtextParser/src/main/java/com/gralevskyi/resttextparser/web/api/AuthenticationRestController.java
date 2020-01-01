package com.gralevskyi.resttextparser.web.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gralevskyi.resttextparser.data.UserRepository;
import com.gralevskyi.resttextparser.domain.User;
import com.gralevskyi.resttextparser.security.AuthenticationRequestDto;
import com.gralevskyi.resttextparser.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping(path = "/rest", produces = "application/json")
@CrossOrigin(origins = "*")
public class AuthenticationRestController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepo;

	public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepo) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userRepo = userRepo;
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
			// throw new BadCredentialsException("Invalid username or password");
			return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
		}

	}

}
