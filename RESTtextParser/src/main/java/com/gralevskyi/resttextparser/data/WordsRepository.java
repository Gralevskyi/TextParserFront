package com.gralevskyi.resttextparser.data;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gralevskyi.resttextparser.domain.SavedWordsList;
import com.gralevskyi.resttextparser.domain.User;

public interface WordsRepository extends CrudRepository<SavedWordsList, Long> {

	List<SavedWordsList> findByUserId(Long userId);

	List<SavedWordsList> findByUserUsername(String username);

	SavedWordsList findByName(String name);

	SavedWordsList findByUserUsernameAndName(String username, String name);

	void deleteById(Long Id);

	void deleteByName(String name);

	@Transactional
	void deleteByUserIdAndName(Long userId, String name);

	@Transactional
	@Modifying
	@Query("update SavedWordsList t set t.name = :newName where t.name = :oldName and t.user = :user")
	void updateWordsListNameByUser(@Param("newName") String newName, @Param("oldName") String oldName, @Param("user") User user);
}
