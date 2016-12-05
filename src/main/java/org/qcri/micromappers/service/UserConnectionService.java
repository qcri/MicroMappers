package org.qcri.micromappers.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.UserConnection;
import org.qcri.micromappers.repository.UserConnectionRepository;
import org.springframework.stereotype.Service;

@Service
public class UserConnectionService
{
  private static Logger logger = Logger.getLogger(UserConnectionService.class);
  @Inject
  private UserConnectionRepository userConnectionRepository;
  
  public UserConnection register(UserConnection userConnection)
  {
    return (UserConnection)this.userConnectionRepository.save(userConnection);
  }
  
  public UserConnection getByProviderIdAndProviderUserId(String providerId, String providerUserId)
  {
    return this.userConnectionRepository.findFirstByProviderIdAndProviderUserId(providerId, providerUserId);
  }
  
  public UserConnection getByProviderIdAndUserId(String providerId, String userId)
  {
    return this.userConnectionRepository.findFirstByProviderIdAndUserId(providerId, userId);
  }
}
