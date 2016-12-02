package org.qcri.micromappers.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
public class HomeController {

	//CurrentUser current_user = new CurrentUser();

	@RequestMapping(value="/")
	public String loadHomePage(Model model){
		return "index";
	}

	@RequestMapping(value="/signin")
	public String signInPage(Model model){

		return "signin";
	}
	
	@RequestMapping(value="/home")
	public String homePage(Model model){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("current_user",name);
		return "home";
	}
	
	/*@RequestMapping(value="/signout")
	public String signOutPage(Model model){

		return "login";
	}*/


}
