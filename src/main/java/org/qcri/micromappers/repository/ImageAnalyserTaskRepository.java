package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.ImageAnalyserTask;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by jlucas on 1/25/17.
 */
public abstract interface ImageAnalyserTaskRepository extends PagingAndSortingRepository<ImageAnalyserTask, Long> {

    List<ImageAnalyserTask> findByState(String state);

}
