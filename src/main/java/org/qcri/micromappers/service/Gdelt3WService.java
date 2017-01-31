package org.qcri.micromappers.service;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Gdelt3W;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.Gdelt3WRepository;
import org.qcri.micromappers.utility.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by jlucas on 1/9/17.
 */
@Service
public class Gdelt3WService {
    private static Logger logger = Logger.getLogger(Gdelt3WService.class);

    @Inject
    private Gdelt3WRepository gdelt3WRepository;

    public Page<Gdelt3W> findbyGlideCode(Integer pageNumber, String glideCode) {

        PageRequest request =
                new PageRequest(pageNumber - 1, Constants.DEFAULT_PAGE_SIZE, Sort.Direction.DESC, "createdAt");

        return gdelt3WRepository.findbyGlideCode(request, glideCode, Constants.GDELT_3W_MMIC_PROCESSED);
    }

    public List<Gdelt3W> findAllByGlideCode(String glideCode){
        return gdelt3WRepository.findAllbyGlideCode(glideCode, Constants.GDELT_3W_MMIC_PROCESSED);
    }

    public List<Gdelt3W> findAllbyState(){
        return gdelt3WRepository.findAllbyState(Constants.GDELT_3W_MMIC_PROCESSED);
    }

    public Gdelt3W saveOrUpdate(Gdelt3W gdelt3W){
        try{
            return gdelt3WRepository.save(gdelt3W);
        }catch (Exception e) {
            logger.error("Error while creating gdelt3W", e);
            throw new MicromappersServiceException("Exception while creating a gdelt3W", e);
        }
    }
}
