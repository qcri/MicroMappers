package org.qcri.micromappers.controller;


import org.apache.log4j.Logger;
import org.qcri.micromappers.service.AccountService;
import org.qcri.micromappers.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	private static Logger logger = Logger.getLogger(HomeController.class);
	
	@Autowired
	AccountService accountService;
	
	@Autowired 
	private Util util;
	
	//CurrentUser current_user = new CurrentUser();

	@RequestMapping(value="/", method = RequestMethod.GET)
	public String loadHomePage(Model model){
		return "index";
	}

	@RequestMapping(value="/signin", method = RequestMethod.GET)
	public String signInPage(Model model){

		return "signin";
	}
	
	@RequestMapping(value="/home", method = RequestMethod.GET)
	public String homePage(Model model){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("current_user",name);
		return "home";
	}
	
	@RequestMapping(value="/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object test(){
		return util.getAuthenticatedUser();
	}

}
