package org.tsir.toll.settings.users.application.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home redirection to swagger api documentation
 */
@Controller
public class HomeController {
	
	@GetMapping(value = "/test")
	public String test() {
		return "redirect:/swagger-ui/";
	}

}
