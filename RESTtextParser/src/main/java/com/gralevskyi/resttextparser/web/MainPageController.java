package com.gralevskyi.resttextparser.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.gralevskyi.resttextparser.domain.SavedWordsList;

@Controller
@RequestMapping("/main")
@SessionAttributes(types = SavedWordsList.class)
public class MainPageController {

	@GetMapping
	public String showMainPage(Model model) {
		model.addAttribute("savedWordsList", new SavedWordsList());
		return "main";
	}

	@PostMapping
	public String redirectToWordsPageController(SavedWordsList savedWordsList) {
		return "redirect:/words";
	}

}