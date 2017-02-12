package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.ComputerVisionRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by jlucas on 2/10/17.
 */
public abstract interface ComputerVisionRequestRepository extends CrudRepository<ComputerVisionRequest, Long> {
    List<ComputerVisionRequest> findByState(String state);
}
