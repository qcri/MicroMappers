package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.Gdelt3W;
import org.qcri.micromappers.entity.GdeltMMIC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by jlucas on 1/9/17.
 */
public abstract interface GdeltMMICRepository extends PagingAndSortingRepository<GdeltMMIC, Long> {
    @Query("SELECT d FROM GdeltMMIC d WHERE d.state=:state and d.glideCode  = :glideCode")
    Page<GdeltMMIC> findbyGlideCode(Pageable pageable, @Param("glideCode") String glideCode, @Param("state") String state);
}
