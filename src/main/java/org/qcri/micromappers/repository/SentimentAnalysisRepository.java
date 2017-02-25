package org.qcri.micromappers.repository;


import java.util.List;

import org.qcri.micromappers.entity.SentimentAnalysis;
import org.qcri.micromappers.utility.TextAnalyticsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by jlucas on 2/17/17.
 */
public abstract interface SentimentAnalysisRepository extends PagingAndSortingRepository<SentimentAnalysis, Long> {

    Page<SentimentAnalysis> findByCollectionIdAndState(Long collectionId, TextAnalyticsStatus state, Pageable pageable);

    List<SentimentAnalysis> findByCollectionIdAndState(Long collectionId, TextAnalyticsStatus state);
}
