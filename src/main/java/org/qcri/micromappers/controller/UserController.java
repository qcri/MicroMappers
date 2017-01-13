package org.qcri.micromappers.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.service.UserConnectionService;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.ResponseCode;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.qcri.micromappers.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Kushal
 */
@Controller
@RequestMapping("/user")
public class UserController {
	protected static Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	UserConnectionService userConnectionService;
	
	@Autowired 
	private Util util;
	
	@RequestMapping(value = "/connectedProviders", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper getAllConnectedProviders() {
		Account authenticatedUser = util.getAuthenticatedUser();
		List<String> distinctProviderIds = null;
		try{
			distinctProviderIds = userConnectionService.getDistinctProviderIdsByUserId(authenticatedUser.getUserName());
		}catch (MicromappersServiceException e) {
			logger.error("Exception while fetching providers for the user: "+authenticatedUser.getUserName());
		}
		
		return new ResponseWrapper(distinctProviderIds, true, ResponseCode.SUCCESS.toString(), null);
	}
	
	/** This method checks whether the user is connected to provider or not before creating the collection.
	 * @param provider
	 * @return the json response valid
	 */
	@RequestMapping(value = "/isProviderConnected", method=RequestMethod.GET)
	@ResponseBody
	public Object isProviderConnected(@RequestParam CollectionType provider) {
		Account authenticatedUser = util.getAuthenticatedUser();
		boolean providerExists = false;
		
		if(provider.equals(CollectionType.ALL)){
			providerExists = (userConnectionService.isProviderConnectedForUser(CollectionType.TWITTER.getValue(), authenticatedUser.getUserName()) && 
					userConnectionService.isProviderConnectedForUser(CollectionType.FACEBOOK.getValue(), authenticatedUser.getUserName()));
		}else{
			providerExists = userConnectionService.isProviderConnectedForUser(provider.getValue(), authenticatedUser.getUserName());
		}
		
		JSONObject response = new JSONObject();
    	response.put("valid", providerExists);
    	return response;
	}
}
