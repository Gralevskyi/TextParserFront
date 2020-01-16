package com.gralevskyi.resttextparser.domain;

import java.util.Map;

public interface TextParser {
	Map<String, Integer> parse(String text);
}
