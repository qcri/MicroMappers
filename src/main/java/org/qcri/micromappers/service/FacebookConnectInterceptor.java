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
package org.qcri.micromappers.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.UserConnection;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

public class FacebookConnectInterceptor implements ConnectInterceptor<Facebook> {

	private static Logger logger = Logger.getLogger(FacebookConnectInterceptor.class);

	@Autowired
	UserConnectionService userConnectionService;
	
	@Autowired
	Util util;
	
	public void preConnect(ConnectionFactory<Facebook> connectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {
	}

	public void postConnect(Connection<Facebook> connection, WebRequest request) {
		Account user = util.getAuthenticatedUser();
		List<UserConnection> connections = userConnectionService.getByProviderIdAndUserIdOrderByRankDesc(CollectionType.FACEBOOK.getValue(), user.getUserName());
		
		//Removing older connections after new connection arrives.
		if(connections.size() > 1){
			for(int index=1; index< connections.size(); index++){
				logger.info("Removing older facebook userConnection for user: "+ user.getUserName());
				userConnectionService.remove(connections.get(index));
			}
		}
	}
}
