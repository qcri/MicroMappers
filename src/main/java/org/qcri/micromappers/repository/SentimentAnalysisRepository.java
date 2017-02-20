package org.qcri.micromappers.repository;


import org.qcri.micromappers.entity.SentimentAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jlucas on 2/17/17.
 */
public abstract interface SentimentAnalysisRepository extends PagingAndSortingRepository<SentimentAnalysis, Long> {

    @Query("SELECT d FROM SentimentAnalysis d WHERE d.state=:state and d.collectionId=:collection_id")
    Page<SentimentAnalysis> findByCollectionIdAndState(Pageable pageable,
                                                       @Param("collection_id")long collection_id,
                                                       @Param("state")String state);

}
