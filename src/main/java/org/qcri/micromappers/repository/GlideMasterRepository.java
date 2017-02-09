package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.GlideMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by jlucas on 12/11/16.PagingAndSortingRepository
 */
public abstract interface GlideMasterRepository extends PagingAndSortingRepository<GlideMaster, Long> {
    Page<GlideMaster> findAll(Pageable pageable);
	GlideMaster findById(Long id);
    GlideMaster findByGlideCode(String glideCode);
    GlideMaster findByComputerVisionEnabled(boolean computerVisionEnabled);
}


