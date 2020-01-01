package com.gralevskyi.resttextparser.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gralevskyi.resttextparser.data.UserRepository;
import com.gralevskyi.resttextparser.data.WordsRepository;
import com.gralevskyi.resttextparser.domain.SavedWordsList;
import com.gralevskyi.resttextparser.domain.User;

@Controller
@RequestMapping("/cabinet")
public class CabinetController {

	WordsRepository wordsRepo;
	UserRepository userRepo;

	@Autowired
	CabinetController(WordsRepository wordsRepo, UserRepository userRepo) {
		this.wordsRepo = wordsRepo;
		this.userRepo = userRepo;
	}

	@GetMapping
	public String returnUserCabinet(Model model, @AuthenticationPrincipal User user) {
		Long userId = userRepo.findByUsername(user.getUsername()).getId();
		model.addAttribute("UserLists", wordsRepo.findByUserId(userId));
		return "cabinet";
	}

	@PostMapping("/savedWordsList")
	public String returnSavedWordsList(Model model, String name, String button, @AuthenticationPrincipal User user) {
		if (button.equalsIgnoreCase("delete")) {
			Long id = wordsRepo.findByName(name).getId();
			wordsRepo.deleteById(id);
			Long userId = userRepo.findByUsername(user.getUsername()).getId();
			model.addAttribute("UserLists", wordsRepo.findByUserId(userId));
			return "cabinet";
		} else {
			SavedWordsList savedWordsList = wordsRepo.findByUserUsernameAndName(user.getUsername(), name);
			model.addAttribute("savedWordsList", savedWordsList);
			return "savedWordsList";
		}
	}
}
