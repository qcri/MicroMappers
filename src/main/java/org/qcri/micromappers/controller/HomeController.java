package org.qcri.micromappers.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.service.AccountService;
import org.qcri.micromappers.service.CollectionService;
import org.qcri.micromappers.utility.ResponseCode;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.qcri.micromappers.utility.Util;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Page;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;

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
		Account account = util.getAuthenticatedUser();
		if(name== null || name.isEmpty()){
			return "signin";
		}
		request.getSession().setAttribute("current_user", name);
		request.getSession().setAttribute("account", account);
		model.addAttribute("current_user",name);
		//return "redirect:settings";
		return "home";
	}

	@RequestMapping(value="/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseWrapper test() throws FacebookException{
		Facebook facebook = getFacebookInstance("EAAZAwtbYIijABAOF5Qt80OF3JJpZCItK55IDUlZCLZCrHMHwWkBGVtS4M7G2QcVZBDNcOZAymrFEUNNTkT9Yc4ZCxbjdgON5hY9Wy8eR9gQglbZAnFE5ECZB7FmwwCUZBIHgEko5vJfkXBYZApbjQT1mIFzYqz4QQ9rl0EZD");
		ResponseList<Page> search = facebook.searchPages("amazon", 
				new Reading().fields("id,name,link,likes.summary(true),fan_count,picture").limit(100).offset(0));
		search.getPaging().getCursors();

		return new ResponseWrapper("Kushal Kant Goyal", true, ResponseCode.SUCCESS.toString(), search.toString());
	}

	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:signin";
	}

	private static Facebook getFacebookInstance(String accessToken) {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setDebugEnabled(false)
		.setOAuthAppId(MicromappersConfigurator.getInstance().getProperty(MicromappersConfigurationProperty.FACEBOOK_APP_KEY))
		.setOAuthAppSecret(
				MicromappersConfigurator.getInstance().getProperty(MicromappersConfigurationProperty.FACEBOOK_APP_SECRET))
		.setJSONStoreEnabled(true).setOAuthAccessToken(accessToken);

		Configuration configuration = builder.build();
		Facebook instance = new FacebookFactory(configuration).getInstance();
		return instance;
	}

}
