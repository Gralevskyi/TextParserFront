package com.gralevskyi.resttextparser.data;

import org.springframework.data.repository.CrudRepository;

import com.gralevskyi.resttextparser.domain.user.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);

}
