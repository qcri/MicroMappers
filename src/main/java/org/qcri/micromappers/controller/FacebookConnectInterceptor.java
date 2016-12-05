/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qcri.micromappers.controller;

import org.apache.log4j.Logger;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

public class FacebookConnectInterceptor implements ConnectInterceptor<Facebook> {

	private static Logger logger = Logger.getLogger(FacebookConnectInterceptor.class);
	
	/*@Autowired
	private UtilService utilService;
	
	@Autowired
	private UserConnectionService userConnectionService;
	@Autowired
	private AccountUserService accountUserService;*/
	
	public void preConnect(ConnectionFactory<Facebook> connectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {
	}

	public void postConnect(Connection<Facebook> connection, WebRequest request) {
		/*try {
			Account authenticatedAccount = utilService.getAuthenticatedAccount();
			UserConnection newUserConnection = userConnectionService.getByProviderIdAndProviderUserId(connection.getKey().getProviderId(), connection.getKey().getProviderUserId());
			Long countByUserConnection = accountUserService.getCountByUserConnection(newUserConnection);
		
			if(countByUserConnection == null || countByUserConnection == 0){
				AccountUser accountUser = new AccountUser();
				accountUser.setAccount(authenticatedAccount);
				accountUser.setUserConnection(newUserConnection);
				accountUserService.create(accountUser);
			}else{
				logger.info("Connection already exists");
			}
		
		} catch (Exception e) {
			logger.info("Exception while connecting a facebbok account");
		}*/
	}
}
