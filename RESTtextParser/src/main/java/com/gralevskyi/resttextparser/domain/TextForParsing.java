package com.gralevskyi.resttextparser.domain;

import javax.validation.constraints.Size;

public class TextForParsing {

	@Size(min = 1, max = 2000, message = "Text for parsing must have at least one and no more then 2 000 letters.")
	String text;

	public TextForParsing(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
