package com.gralevskyi.resttextparser.data;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gralevskyi.resttextparser.domain.TextAndParsedWordsUnit;
import com.gralevskyi.resttextparser.domain.user.User;

public interface WordsRepository extends CrudRepository<TextAndParsedWordsUnit, Long> {

	List<TextAndParsedWordsUnit> findByUserUsername(String username);

	TextAndParsedWordsUnit findByUserUsernameAndName(String username, String name);

	@Transactional
	void deleteByUserUsernameAndName(String username, String name);

	@Transactional
	@Modifying
	@Query("update TextAndParsedWordsUnit t set t.name = :newName where t.name = :oldName and t.user = :user")
	void updateWordsUnitNameByUser(@Param("newName") String newName, @Param("oldName") String oldName, @Param("user") User user);
}
