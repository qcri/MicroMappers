package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.CollectionLabel;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Kushal
 *
 */
public abstract interface CollectionLabelRepository extends CrudRepository<CollectionLabel, Long>
{

	CollectionLabel findByCollectionId(Long collectionId);
}
