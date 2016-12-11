package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jlucas on 12/11/16.
 */
public abstract interface GlobalEventDefinitionRepository extends CrudRepository<GlobalEventDefinition, Long>{
    GlobalEventDefinition findByEventUrl(String eventUrl);
}


