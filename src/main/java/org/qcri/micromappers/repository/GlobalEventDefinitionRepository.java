package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by jlucas on 12/11/16.PagingAndSortingRepository
 */
public abstract interface GlobalEventDefinitionRepository extends PagingAndSortingRepository<GlobalEventDefinition, Long> {
    GlobalEventDefinition findByEventUrl(String eventUrl);
    Page<GlobalEventDefinition> findAll(Pageable pageable);
	GlobalEventDefinition findById(Long id);
}


