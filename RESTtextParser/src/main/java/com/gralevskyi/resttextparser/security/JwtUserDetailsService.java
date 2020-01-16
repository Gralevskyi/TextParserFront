package com.gralevskyi.resttextparser.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gralevskyi.resttextparser.data.UserRepository;
import com.gralevskyi.resttextparser.domain.user.User;
import com.gralevskyi.resttextparser.security.jwt.JwtUser;
import com.gralevskyi.resttextparser.security.jwt.JwtUserFactory;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private UserRepository userRepo;

	@Autowired
	public JwtUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (user != null) {
			JwtUser jwtUser = JwtUserFactory.create(user);
			return jwtUser;
		}
		throw new UsernameNotFoundException("User '" + username + "' not found");
	}

	public boolean isUsernameAlreadyExist(String username) {
		User user = userRepo.findByUsername(username);
		if (user != null) {
			return true;
		}
		return false;
	}

	public String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			return currentUserName;
		}
		return null;
	}

}
