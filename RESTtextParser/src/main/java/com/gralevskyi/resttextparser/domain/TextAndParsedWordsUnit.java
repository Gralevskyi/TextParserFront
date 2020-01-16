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

import com.gralevskyi.resttextparser.constraints.UniqueWordsUnitNameForCurrentUser;
import com.gralevskyi.resttextparser.domain.user.User;

@Entity
public class TextAndParsedWordsUnit {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 2000)
	private String userText;

	@ElementCollection
	private Map<String, Integer> parsedWords;

	@NotNull
	@Size(min = 5, message = "Name must be at least 5 character long.")
	@UniqueWordsUnitNameForCurrentUser
	private String name;

	@ManyToOne
	User user;

	public TextAndParsedWordsUnit(String userText, Map<String, Integer> parsedWords, String name) {
		this.userText = userText;
		this.parsedWords = parsedWords;
		this.name = name;
	}

	public TextAndParsedWordsUnit() {

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

	public Map<String, Integer> getParsedWords() {
		return parsedWords;
	}

	public void setParsedWords(Map<String, Integer> parsedWords) {
		this.parsedWords = parsedWords;
	}

}
