package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.ImageAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by jlucas on 1/24/17.
 */
public abstract interface ImageAnalysisRepository extends PagingAndSortingRepository<ImageAnalysis, Long> {
    Page<ImageAnalysis> findAll(Pageable pageable);
    ImageAnalysis findByImageURL(String imageURL);
}
