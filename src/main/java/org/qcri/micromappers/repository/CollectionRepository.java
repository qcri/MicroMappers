package org.qcri.micromappers.repository;

import javax.transaction.Transactional;

import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.utility.CollectionStatus;
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
    @Query("UPDATE Collection c SET c.status = :status WHERE c.code = :code")
	public int updateStatusByCode(@Param("code") String code, @Param("status") CollectionStatus status);
	
	@Transactional
	@Modifying
    @Query("UPDATE Collection c SET c.status = :status WHERE c.id = :id")
	public int updateStatusById(@Param("id") Long id, @Param("status") CollectionStatus status);
}
