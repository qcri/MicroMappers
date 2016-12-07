package org.qcri.micromappers.controller;


import org.apache.log4j.Logger;
import org.qcri.micromappers.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	private static Logger logger = Logger.getLogger(HomeController.class);
	
	@Autowired
	AccountService accountService;
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
