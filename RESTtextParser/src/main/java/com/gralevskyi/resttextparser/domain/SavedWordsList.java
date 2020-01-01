package com.gralevskyi.resttextparser.domain;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gralevskyi.resttextparser.constraints.UniqueWordsListNameForCurrentUser;

import lombok.Data;

@Data
@Entity
public class SavedWordsList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 2000)
	@Size(max = 2000)
	private String userText;

	@ElementCollection
	private Map<String, Integer> wordsMap;

	@NotNull
	@Size(min = 5, message = "Name must be at least 5 character long")
	@UniqueWordsListNameForCurrentUser
	private String name;

	@ManyToOne
	User user;

	public SavedWordsList(String userText, Map<String, Integer> wordsMap, String name) {
		this.userText = userText;
		this.wordsMap = wordsMap;
		this.name = name;
	}

	public SavedWordsList() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUser(User user) {
		this.user = user;

	}

	public String getName() {
		return name;
	}

	public String getUserText() {
		return userText;
	}

	public void setUserText(String userText) {
		this.userText = userText;
	}

	public Map<String, Integer> getWordsMap() {
		return wordsMap;
	}

	public void setWordsMap(Map<String, Integer> wordsMap) {
		this.wordsMap = wordsMap;
	}

}
