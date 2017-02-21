package org.qcri.micromappers.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.ComputerVisionRequest;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.GlideMasterRepository;
import org.qcri.micromappers.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jlucas on 12/11/16.
 */
@Service
public class GlideMasterService {
    private static Logger logger = Logger.getLogger(GlideMasterService.class);

    @Inject
    private GlideMasterRepository glideMasterRepository;


    @Autowired
    ComputerVisionRequestService computerVisionRequestService;


    public Page<GlideMaster> listAllByPage(Integer pageNumber) {
        PageRequest request =
                new PageRequest(pageNumber - 1, Constants.DEFAULT_PAGE_SIZE, Sort.Direction.DESC, "updated");

        Page<GlideMaster> pages = glideMasterRepository.findAll(request);
        pages = this.resetPageConent(pages);
        return pages;
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

    public GlideMaster getByGlideCode(String glideCode){
        return glideMasterRepository.findByGlideCode(glideCode);
    }

    public GlideMaster findByComputerVisionEnabled(boolean computerVisionEnabled){
        return glideMasterRepository.findByComputerVisionEnabled(computerVisionEnabled);
    }

    public List<GlideMaster> findAll(){
        return (List<GlideMaster>) glideMasterRepository.findAll();
    }

    public List<GlideMaster> findAllBySearchKey(String searchKeyWord){
        String word = "%" + searchKeyWord.toUpperCase() + "%";
        return (List<GlideMaster>) glideMasterRepository.findAllByKeyWord(word);
    }

    public Page<GlideMaster> resetPageConent(Page<GlideMaster> pages){
        List<GlideMaster> glideMasterList = pages.getContent();

        glideMasterList.forEach(item->{
            this.getComputerVisionApprovalState(item);
            this.getComputerVisionRejectState(item);
            this.getComputerVisionOnRequestState(item);
        });

        return pages;

    }

    private GlideMaster getComputerVisionRejectState(GlideMaster glideMaster){
        List<ComputerVisionRequest> rejectedRequest = computerVisionRequestService.findComputerVisionRequestOnRejected();
        rejectedRequest.forEach(item->{
            if(item.getGlideMasterId() == glideMaster.getId()){
                glideMaster.setComputerVisionRequestState(Constants.COMPUTER_VISION_REJECTED);
            }
        });

        return glideMaster;
    }

    private GlideMaster getComputerVisionApprovalState(GlideMaster glideMaster){
        List<ComputerVisionRequest> approvedRequest = computerVisionRequestService.findComputerVisionRequestOnRunning();
        approvedRequest.forEach(item->{
            if(item.getGlideMasterId() == glideMaster.getId()){
                glideMaster.setComputerVisionRequestState(Constants.COMPUTER_VISION_APPROVED);
            }
        });

        return glideMaster;
    }

    private GlideMaster getComputerVisionOnRequestState(GlideMaster glideMaster){
        List<ComputerVisionRequest> pendingRequest =computerVisionRequestService.findComputerVisionRequestOnRequest();
        pendingRequest.forEach(item->{
            if(item.getGlideMasterId() == glideMaster.getId()){
                glideMaster.setComputerVisionRequestState(Constants.COMPUTER_VISION_ON_REQUEST);
            }
        });

        return glideMaster;
    }
}
