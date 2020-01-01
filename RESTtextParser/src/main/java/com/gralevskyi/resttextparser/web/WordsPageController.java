package com.gralevskyi.resttextparser.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;

import com.gralevskyi.resttextparser.data.WordsRepository;
import com.gralevskyi.resttextparser.domain.SavedWordsList;
import com.gralevskyi.resttextparser.domain.TextParser;
import com.gralevskyi.resttextparser.domain.User;

@Controller
@RequestMapping("/words")
//@SessionAttributes(types = SavedWordsList.class)
public class WordsPageController {

	@Autowired
	TextParser textParser;

	WordsRepository wordsRepo;

	@Autowired
	WordsPageController(WordsRepository wordsRepo) {
		this.wordsRepo = wordsRepo;
	}

	@GetMapping
	public String showWordsPage(Model model, @SessionAttribute(name = "savedWordsList") SavedWordsList savedWordsList) {
		Map<String, Integer> wordsMap = textParser.parse(savedWordsList.getUserText());
		savedWordsList.setWordsMap(wordsMap);
		model.addAttribute("savedWordsList", savedWordsList);
		return "words";
	}

	@PostMapping
	public String saveWords(Model model, @SessionAttribute(name = "savedWordsList") SavedWordsList savedWordsList, @ModelAttribute(name = "savedWordsList") @Valid SavedWordsList modelSavedWordsList, Errors errors, SessionStatus sessionStatus,
			@AuthenticationPrincipal User user) {
		if (errors.hasErrors()) {
			modelSavedWordsList.setWordsMap(savedWordsList.getWordsMap());
			model.addAttribute("savedWordsList", modelSavedWordsList);
			return "words";
		}
		savedWordsList.setName(modelSavedWordsList.getName());
		savedWordsList.setUser(user);
		wordsRepo.save(savedWordsList);
		sessionStatus.setComplete();
		return "redirect:/cabinet";
	}

}