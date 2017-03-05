package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.DataFeed;
import org.qcri.micromappers.entity.Gdelt3W;
import org.qcri.micromappers.entity.GdeltMMIC;
import org.qcri.micromappers.entity.ImageAnalyserTask;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jlucas on 1/25/17.
 */
public abstract interface ImageAnalyserTaskRepository extends PagingAndSortingRepository<ImageAnalyserTask, Long> {

    List<ImageAnalyserTask> findByState(String state);
    List<ImageAnalyserTask> findByGdelt3W(Gdelt3W gdelt3W);
    List<ImageAnalyserTask> findByGdeltMMIC(GdeltMMIC gdeltMMIC);
    List<ImageAnalyserTask> findByDataFeed(DataFeed dataFeed);
    List<ImageAnalyserTask> findByImageURL(String imgURL);
}
