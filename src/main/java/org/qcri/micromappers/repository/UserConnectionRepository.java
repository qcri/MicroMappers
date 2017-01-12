package org.qcri.micromappers.repository;

import java.util.List;

import org.qcri.micromappers.entity.UserConnection;
import org.springframework.data.repository.CrudRepository;

public abstract interface UserConnectionRepository extends CrudRepository<UserConnection, Integer>
{
  public abstract UserConnection findFirstByProviderIdAndUserIdOrderByRankDesc(String providerId, String userId);
  
  public abstract UserConnection findFirstByProviderIdAndProviderUserId(String providerId, String providerUserId);

  public abstract List<UserConnection> findByProviderIdAndUserIdOrderByRankDesc(String providerId, String userId);

  public abstract List<UserConnection> findDistinctProviderIdByUserId(String userId);
}
