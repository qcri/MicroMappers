package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.TextDisambiguityAnalysis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Kushal
 *
 */
public abstract interface TextDisambiguityRepository extends CrudRepository<TextDisambiguityAnalysis, Long>
{
    List<TextDisambiguityAnalysis> findByCollectionId(Long collectionId);
}
