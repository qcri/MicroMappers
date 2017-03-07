package org.qcri.micromappers.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collaborator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public abstract interface CollaboratorRepository extends CrudRepository<Collaborator, Long>
{
	public Long countByCollectionIdAndAccountId(Long collectionId, Long accountId);

	public List<Collaborator> findByCollectionId(Long collectionId);

	public Collaborator findByCollectionIdAndAccountId(Long collectionId, Long accountId);

	@Transactional
	public Long deleteByCollectionIdAndAccountId(Long collectionId, Long accountId);

	public Page<Collaborator> findByAccount(Account account, Pageable pageable);

	public List<Collaborator> findByAccount(Account account);

	public List<Collaborator> findAll();
}
