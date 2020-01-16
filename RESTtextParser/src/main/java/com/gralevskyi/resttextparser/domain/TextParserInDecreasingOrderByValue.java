package com.gralevskyi.resttextparser.domain;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class TextParserInDecreasingOrderByValue implements TextParser {

	@Override
	public Map<String, Integer> parse(String text) {
		Map<String, Integer> parsedWords = new HashMap<String, Integer>();
		String[] wordsArray = text.split("[^a-zA-Z]+");

		// each word is modified to lower case to avoid duplicates
		for (String word : wordsArray) {
			int indexOfWordInArray = Arrays.asList(wordsArray).indexOf(word);
			wordsArray[indexOfWordInArray] = word.toLowerCase();
		}

		// fill parsedWords map with words as a key and the number of their occurrences
		// in the text as a value
		for (String word : wordsArray) {
			if (parsedWords.containsKey(word)) {
				int numberOfTimesWordAppearInText = parsedWords.get(word);
				parsedWords.put(word, ++numberOfTimesWordAppearInText);
			} else {
				int numberOfTimesWordAppearInText = 1;
				parsedWords.put(word, numberOfTimesWordAppearInText);
			}

			parsedWords.remove(""); // removes empty key from parsed words map
		}
		return decreasingSortByValue(parsedWords);
	}

	// sort a map by value in decreasing order
	Map<String, Integer> decreasingSortByValue(Map<String, Integer> unsorted) {
		Map<String, Integer> sorted = unsorted.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		return sorted;
	}
}
