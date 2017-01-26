package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.CollectionLog;
import org.qcri.micromappers.utility.CollectionType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public abstract interface CollectionLogRepository extends CrudRepository<CollectionLog, Long>
{
	CollectionLog findFirstByCollectionIdAndProviderOrderByStartDateDesc(Long collectionId, CollectionType provider);

	@Query("SELECT sum(cl.count) FROM CollectionLog cl WHERE cl.collection.id = :collectionId and cl.provider = :provider")
	public Long getCollectionCountByCollectionIdAndProvider(@Param("collectionId") Long collectionId, @Param("provider") CollectionType provider);
}
