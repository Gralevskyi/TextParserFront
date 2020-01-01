package com.gralevskyi.resttextparser.domain;

import javax.validation.constraints.Size;

public class TextDto {

	@Size(max = 2000, message = "Text for parsing is limited to 2 000 letters")
	String text;

	public TextDto(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
