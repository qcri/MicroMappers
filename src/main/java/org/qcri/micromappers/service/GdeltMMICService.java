package org.qcri.micromappers.service;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Gdelt3W;
import org.qcri.micromappers.entity.GdeltMMIC;
import org.qcri.micromappers.repository.Gdelt3WRepository;
import org.qcri.micromappers.repository.GdeltMMICRepository;
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
public class GdeltMMICService {
    private static Logger logger = Logger.getLogger(GdeltMMICService.class);

    @Inject
    private GdeltMMICRepository gdeltMMICRepository;

    public Page<GdeltMMIC> findbyGlideCode(Integer pageNumber, String glideCode) {

        PageRequest request =
                new PageRequest(pageNumber - 1, Constants.DEFAULT_PAGE_SIZE, Sort.Direction.DESC, "createdAt");

        return gdeltMMICRepository.findbyGlideCode(request, glideCode, "processed");
    }

    public List<GdeltMMIC> findAllByGlideCode(String glideCode){
        return gdeltMMICRepository.findAllbyGlideCode(glideCode, "processed");
    }
}
