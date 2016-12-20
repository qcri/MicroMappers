package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.CollectionLog;
import org.springframework.data.repository.CrudRepository;

public abstract interface CollectionLogRepository extends CrudRepository<CollectionLog, Long>
{
	CollectionLog findFirstByCollectionIdOrderByStartDateDesc(Long collectionId);
}
