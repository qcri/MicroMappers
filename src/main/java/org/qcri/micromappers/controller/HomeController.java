package org.qcri.micromappers.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import org.qcri.micromappers.model.CurrentUser;

@org.springframework.stereotype.Controller
public class HomeController {

	CurrentUser current_user = new CurrentUser();

	@RequestMapping(value="/")
	public String loadHomePage(Model model){
		model.addAttribute("current_user", current_user);

		return "index";
	}

	@RequestMapping(value="/signin")
	public String signInPage(Model model){
		model.addAttribute("current_user", current_user);

		return "signin";
	}


}
