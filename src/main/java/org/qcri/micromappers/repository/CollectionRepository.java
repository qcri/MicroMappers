package org.qcri.micromappers.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.utility.CollectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public abstract interface CollectionRepository extends CrudRepository<Collection, Long>
{
	public Collection findById(Long id);

	public Collection findByCode(String code);
	
	@Transactional
	@Modifying
    @Query("UPDATE Collection c SET c.facebookStatus = :facebookStatus WHERE c.id = :id")
	public int updateFacebookStatusById(@Param("id") Long id,  @Param("facebookStatus") CollectionStatus facebookStatus);
	
	@Transactional
	@Modifying
    @Query("UPDATE Collection c SET c.facebookStatus = :facebookStatus, c.lastExecutionTime = :lastExecutionTime WHERE c.id = :id")
	public int updateFacebookStatusAndLastExecutionTimeById(@Param("id") Long id, @Param("facebookStatus") CollectionStatus facebookStatus, @Param("lastExecutionTime") Date lastExecutionTime);
	
	@Transactional
	@Modifying
    @Query("UPDATE Collection c SET c.twitterStatus = :twitterStatus WHERE c.id = :id")
	public int updateTwitterStatusById(@Param("id") Long id, @Param("twitterStatus") CollectionStatus twitterStatus);
	
	@Transactional
	@Modifying
    @Query("UPDATE Collection c SET c.twitterStatus = :twitterStatus, c.facebookStatus = :facebookStatus, c.isTrashed = :isTrashed WHERE c.id = :id")
	public int updatingTwitterStatusAndFacebookStatusAndTrashStatusById(@Param("id") Long id, @Param("twitterStatus") CollectionStatus twitterStatus, @Param("facebookStatus") CollectionStatus facebookStatus, @Param("isTrashed") Boolean isTrashed);
	
	public List<Collection> findByTwitterStatusIn(List<CollectionStatus> statusList);

	public Long countByNameIgnoreCase(String name);

	public Page<Collection> findByAccount(Account account, Pageable page);

	public Collection getByAccountAndGlobalEventDefinition(Account account, GlobalEventDefinition globalEventDefinition);

	public Collection getByAccountAndGlideMaster(Account account,GlideMaster glideMaster);
}
