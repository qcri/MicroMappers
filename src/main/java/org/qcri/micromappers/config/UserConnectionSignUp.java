package org.qcri.micromappers.config;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.support.OAuth1Connection;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.stereotype.Component;

@Component
public class UserConnectionSignUp
  implements ConnectionSignUp
{
  public String execute(Connection<?> connection)
  {
    if ((connection instanceof OAuth1Connection))
    {
      OAuth1Connection<?> oauthConnection = (OAuth1Connection)connection;
      ConnectionData data = oauthConnection.createData();
      UserProfile profile = oauthConnection.fetchUserProfile();
     /* UserConnection userConnection = new UserConnection();
      userConnection.setUserId(profile.getUsername());
      userConnection.setImageUrl(data.getImageUrl());
      userConnection.setProfileUrl(data.getProfileUrl());
      userConnection.setAccessToken(data.getAccessToken());
      userConnection.setSecret(data.getSecret());
      userConnection.setRefreshToken(data.getRefreshToken());
      userConnection.setProviderUserId(data.getProviderUserId());

      userConnection.setProviderId("springSocialSecurity");
      userConnection.setDisplayName(data.getDisplayName());
      userConnection.setRank(1);
      UserConnection registerdUserConnection = this.userConnectionService.register(userConnection);
      
      Account account = new Account();
      account.setLocale("en");
      account.setApiKey(UUID.randomUUID().toString());
      account.setRole(RoleType.NORMAL);
      account.setUserName(registerdUserConnection.getUserId() + Constants.USER_NAME_SPLITTER + System.currentTimeMillis());
      Account registeredAccount = this.accountService.create(account);
      
      AccountUser accountUser = new AccountUser();
      accountUser.setAccount(registeredAccount);
      accountUser.setUserConnection(registerdUserConnection);
      this.accountUserService.create(accountUser);*/
      
      return profile.getUsername();
    }
    if ((connection instanceof OAuth2Connection))
    {
      OAuth2Connection<?> oauthConnection = (OAuth2Connection)connection;
      ConnectionData data = oauthConnection.createData();
      UserProfile profile = oauthConnection.fetchUserProfile();
      
      /* UserConnection userConnection = new UserConnection();
      userConnection.setUserId(profile.getEmail());
      userConnection.setImageUrl(data.getImageUrl());
      userConnection.setProfileUrl(data.getProfileUrl());
      userConnection.setAccessToken(data.getAccessToken());
      userConnection.setSecret(data.getSecret());
      userConnection.setRefreshToken(data.getRefreshToken());
      userConnection.setProviderUserId(data.getProviderUserId());
      userConnection.setProviderId("springSocialSecurity");
      userConnection.setDisplayName(data.getDisplayName());
      userConnection.setExpireTime(data.getExpireTime());
      userConnection.setRank(1);
      UserConnection registerdUserConnection = this.userConnectionService.register(userConnection);
      
      Account account = new Account();
      account.setLocale("en");
      account.setApiKey(UUID.randomUUID().toString());
      account.setRole(RoleType.NORMAL);
      account.setUserName(registerdUserConnection.getUserId() + Constants.USER_NAME_SPLITTER + System.currentTimeMillis());
      Account registeredAccount = this.accountService.create(account);
      
      AccountUser accountUser = new AccountUser();
      accountUser.setAccount(registeredAccount);
      accountUser.setUserConnection(registerdUserConnection);
      this.accountUserService.create(accountUser);*/
      
      return profile.getEmail();
    }
    return connection.fetchUserProfile().getUsername();
  }
}
