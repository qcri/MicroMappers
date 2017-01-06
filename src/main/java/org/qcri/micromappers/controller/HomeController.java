package org.qcri.micromappers.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.qcri.micromappers.service.AccountService;
import org.qcri.micromappers.service.CollectionService;
import org.qcri.micromappers.utility.ResponseCode;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.qcri.micromappers.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
	CollectionService collectionService;
	
	@Autowired 
	private Util util;
	
	//CurrentUser current_user = new CurrentUser();

	@RequestMapping(value="/", method = RequestMethod.GET)
	public String loadHomePage(Model model, HttpServletRequest request){
		return "index";
	}

	@RequestMapping(value="/signin", method = RequestMethod.GET)
	public String signInPage(Model model, HttpServletRequest request){
		return "signin";
	}

	@RequestMapping(value="/settings", method = RequestMethod.GET)
	public String settingsInPage(Model model, HttpServletRequest request){
		return "settings";
	}
	
	@RequestMapping(value="/home", method = RequestMethod.GET)
	public String homePage(Model model, HttpServletRequest request){
		String name = util.getAuthenticatedUserName();
		if(name== null || name.isEmpty()){
			return "signin";
		}
		request.getSession().setAttribute("current_user", name);
		model.addAttribute("current_user",name);
		//return "redirect:settings";
		return "home";
	}
	
	@RequestMapping(value="/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseWrapper test(){
		
		return new ResponseWrapper("Kushal Kant Goyal", true, ResponseCode.SUCCESS.toString(), "KKG");
	}

	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:signin";
	}
	
}
