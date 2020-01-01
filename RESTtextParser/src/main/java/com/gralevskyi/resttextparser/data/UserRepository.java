package com.gralevskyi.resttextparser.data;

import org.springframework.data.repository.CrudRepository;

import com.gralevskyi.resttextparser.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);

}
