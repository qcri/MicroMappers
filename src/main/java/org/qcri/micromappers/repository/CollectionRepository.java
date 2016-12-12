package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.Collection;
import org.springframework.data.repository.CrudRepository;

public abstract interface CollectionRepository extends CrudRepository<Collection, Long>
{
}
