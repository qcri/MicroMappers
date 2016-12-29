package org.qcri.micromappers.service;

import java.util.List;

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
		return userConnectionRepository.save(userConnection);
	}

	public UserConnection getByProviderIdAndProviderUserId(String providerId, String providerUserId)
	{
		return userConnectionRepository.findFirstByProviderIdAndProviderUserId(providerId, providerUserId);
	}

	public UserConnection getByProviderIdAndUserId(String providerId, String userId)
	{
		return userConnectionRepository.findFirstByProviderIdAndUserIdOrderByRankDesc(providerId, userId);
	}

	public List<UserConnection> getByProviderIdAndUserIdOrderByRankDesc(String providerId, String userId) {
		return userConnectionRepository.findByProviderIdAndUserIdOrderByRankDesc(providerId, userId);
	}

	public Boolean remove(UserConnection userConnection) {
		try{
			userConnectionRepository.delete(userConnection);
			return Boolean.TRUE;
		}catch(Exception e){
			logger.error("Exception while deleteing the userConnection for user: "+ userConnection.getUserId());
			return Boolean.FALSE;
		}
	}
}
