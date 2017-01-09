package org.qcri.micromappers.service;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.models.GdeltMaster;
import org.qcri.micromappers.repository.GlideMasterRepository;
import org.qcri.micromappers.utility.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Created by jlucas on 12/11/16.
 */
@Service
public class GlideMasterService {
    private static Logger logger = Logger.getLogger(GlideMasterService.class);

    @Inject
    private GlideMasterRepository glideMasterRepository;


    public Page<GlideMaster> listAllByPage(Integer pageNumber) {
        PageRequest request =
                new PageRequest(pageNumber - 1, Constants.DEFAULT_PAGE_SIZE, Sort.Direction.DESC, "updated");

        return glideMasterRepository.findAll(request);
    }
}
