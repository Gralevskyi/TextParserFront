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
	public Map<String, Integer> parse(String s) {
		Map<String, Integer> wordsMap = new HashMap<String, Integer>();
		// "if" is used to avoid error when the method is called without pasting a
		// string
		if (s != null) {
			String[] wordsArray = s.split("[^a-zA-Z]+");
			for (String word : wordsArray) {
				int indexOfWordInArray = Arrays.asList(wordsArray).indexOf(word);
				wordsArray[indexOfWordInArray] = word.toLowerCase();
			}

			for (String word : wordsArray) {
				if (wordsMap.containsKey(word)) {
					int i = wordsMap.get(word);
					wordsMap.put(word, ++i);
				} else {
					wordsMap.put(word, 1);
				}

			}
			wordsMap.remove(""); // removes spaces as key in map
		}
		return decreasingSortByValue(wordsMap);
	}

	// sort a map in decreasing order of value
	Map<String, Integer> decreasingSortByValue(Map<String, Integer> unsorted) {
		Map<String, Integer> sorted = unsorted.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		return sorted;
	}
}
