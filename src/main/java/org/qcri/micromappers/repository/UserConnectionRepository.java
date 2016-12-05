package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.UserConnection;
import org.springframework.data.repository.CrudRepository;

public abstract interface UserConnectionRepository extends CrudRepository<UserConnection, Integer>
{
  public abstract UserConnection findFirstByProviderIdAndUserId(String providerId, String userId);
  
  public abstract UserConnection findFirstByProviderIdAndProviderUserId(String providerId, String providerUserId);
}
