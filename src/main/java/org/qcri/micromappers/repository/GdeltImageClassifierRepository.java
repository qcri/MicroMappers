package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.GdeltImageClassifier;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by jlucas on 4/30/17.
 */
public abstract interface GdeltImageClassifierRepository extends CrudRepository<GdeltImageClassifier, Long> {

    public List<GdeltImageClassifier> findByAccount(Account account);
    public List<GdeltImageClassifier> findByState(String state);
}
