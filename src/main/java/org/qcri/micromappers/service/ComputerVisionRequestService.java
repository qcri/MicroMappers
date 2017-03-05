package org.qcri.micromappers.service;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.ComputerVisionRequest;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.ComputerVisionRequestRepository;
import org.qcri.micromappers.utility.Constants;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by jlucas on 2/10/17.
 */
@Service
public class ComputerVisionRequestService {

    private static Logger logger = Logger.getLogger(ComputerVisionRequestService.class);

    @Inject
    ComputerVisionRequestRepository computerVisionRequestRepository;

    public ComputerVisionRequest saveOrUpdate(ComputerVisionRequest computerVisionRequest)
    {
        try{

            return computerVisionRequestRepository.save(computerVisionRequest);
        }catch (Exception e) {
            logger.error("Error while creating ComputerVisionRequest", e);
            throw new MicromappersServiceException("Exception while creating a ComputerVisionRequest", e);
        }
    }

    public List<ComputerVisionRequest> findComputerVisionRequestOnRunning(){
        return computerVisionRequestRepository.findByState(Constants.COMPUTER_VISION_APPROVED);
    }

    public List<ComputerVisionRequest> findComputerVisionRequestOnRequest(){
        return computerVisionRequestRepository.findByState(Constants.COMPUTER_VISION_ON_REQUEST);
    }

    public List<ComputerVisionRequest> findComputerVisionRequestOnRejected(){
        return computerVisionRequestRepository.findByState(Constants.COMPUTER_VISION_REJECTED);
    }

}

