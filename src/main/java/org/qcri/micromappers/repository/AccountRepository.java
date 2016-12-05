package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.Account;
import org.springframework.data.repository.CrudRepository;

public abstract interface AccountRepository extends CrudRepository<Account, Long>
{
	Account findByUserName(String userName);
}
