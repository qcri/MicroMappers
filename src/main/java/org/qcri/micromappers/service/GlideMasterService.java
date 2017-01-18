package org.qcri.micromappers.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.GlideMasterRepository;
import org.qcri.micromappers.utility.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
                new PageRequest(pageNumber - 1, Integer.parseInt(Constants.DEFAULT_PAGE_SIZE), Sort.Direction.DESC, "updated");

        return glideMasterRepository.findAll(request);
    }
    
    public GlideMaster getById(Long id)
	{
		try{
			return glideMasterRepository.findById(id);
		}catch (Exception e) {
			logger.error("Error while fetching glide master by id", e);
			throw new MicromappersServiceException("Error while fetching glide master by id", e);
		}
	}
}
