package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.TextDisambiguityAnalysis;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Kushal
 *
 */
public abstract interface TextDisambiguityRepository extends CrudRepository<TextDisambiguityAnalysis, Long>
{
}
