package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.Gdelt3W;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jlucas on 1/9/17.
 */
public abstract interface Gdelt3WRepository extends PagingAndSortingRepository<Gdelt3W, Long> {
    @Query("SELECT d FROM Gdelt3W d WHERE d.state=:state and d.glideCode  = :glideCode")
    Page<Gdelt3W> findbyGlideCode(Pageable pageable, @Param("glideCode")String glideCode, @Param("state")String state);

    @Query("SELECT d FROM Gdelt3W d WHERE d.state=:state and d.glideCode  = :glideCode")
    List<Gdelt3W> findAllbyGlideCode(@Param("glideCode")String glideCode, @Param("state")String state);

    @Query("SELECT d FROM Gdelt3W d WHERE d.state=:state")
    List<Gdelt3W> findAllbyState(@Param("state")String state);
}
