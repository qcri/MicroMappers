package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.CollectionLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public abstract interface CollectionLogRepository extends CrudRepository<CollectionLog, Long>
{
	CollectionLog findFirstByCollectionIdOrderByStartDateDesc(Long collectionId);

	@Query("SELECT sum(cl.count) FROM CollectionLog cl WHERE collection.id = :collectionId")
	public Long getCollectionCountByCollectionId(@Param("collectionId") Long collectionId);
}
