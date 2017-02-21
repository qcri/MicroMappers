package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.GlideMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jlucas on 12/11/16.PagingAndSortingRepository
 */
public abstract interface GlideMasterRepository extends PagingAndSortingRepository<GlideMaster, Long> {
    Page<GlideMaster> findAll(Pageable pageable);
    @Query("SELECT d FROM GlideMaster d WHERE d.glideCode  like :searchKey")
    List<GlideMaster> findAllByKeyWord(@Param("searchKey")String searchKey);
	GlideMaster findById(Long id);
    GlideMaster findByGlideCode(String glideCode);
    GlideMaster findByComputerVisionEnabled(boolean computerVisionEnabled);
}


