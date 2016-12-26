package org.qcri.micromappers.config;

import java.util.UUID;

import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.service.AccountService;
import org.qcri.micromappers.utility.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.support.OAuth1Connection;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.stereotype.Component;

@Component
public class UserConnectionSignUp implements ConnectionSignUp
{
	@Autowired
	AccountService accountService;
	public String execute(Connection<?> connection)
	{
		if ((connection instanceof OAuth1Connection))
		{
			OAuth1Connection<?> oauthConnection = (OAuth1Connection)connection;
			ConnectionData data = oauthConnection.createData();
			UserProfile profile = oauthConnection.fetchUserProfile();
			
			Account account = accountService.getByUserName(profile.getEmail());
			if(account == null){
				account = new Account();
				account.setLocale("en");
				account.setApiKey(UUID.randomUUID().toString());
				account.setRole(RoleType.NORMAL);
				account.setUserName(profile.getEmail());
				account.setMailEnabled(Boolean.FALSE);
				accountService.create(account);
			}
			return profile.getUsername();
		}
		if ((connection instanceof OAuth2Connection))
		{
			OAuth2Connection<?> oauthConnection = (OAuth2Connection)connection;
			ConnectionData data = oauthConnection.createData();
			UserProfile profile = oauthConnection.fetchUserProfile();

			Account account = accountService.getByUserName(profile.getEmail());
			if(account == null){
				account = new Account();
				account.setLocale("en");
				account.setApiKey(UUID.randomUUID().toString());
				account.setRole(RoleType.NORMAL);
				account.setUserName(profile.getEmail());
				account.setMailEnabled(Boolean.FALSE);
				accountService.create(account);
			}
			return profile.getEmail();
		}
		return connection.fetchUserProfile().getUsername();
	}
}
