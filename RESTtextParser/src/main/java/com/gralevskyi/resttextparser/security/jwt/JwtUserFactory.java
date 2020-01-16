package com.gralevskyi.resttextparser.security.jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.gralevskyi.resttextparser.domain.user.Role;
import com.gralevskyi.resttextparser.domain.user.User;
import com.gralevskyi.resttextparser.domain.user.UserStatus;

public final class JwtUserFactory {

	public JwtUserFactory() {
	}

	public static JwtUser create(User user) {
		return new JwtUser(user.getId(), user.getUsername(), user.getPassword(), user.getFullname(), user.getEmail(), user.getPhone(), user.getStatus().equals(UserStatus.ACTIVE), mapToGrantedAuthorities(new ArrayList<>(user.getRoles())));
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}
