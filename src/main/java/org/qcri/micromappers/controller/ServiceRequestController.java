package org.qcri.micromappers.controller;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.ComputerVisionRequest;
import org.qcri.micromappers.service.ComputerVisionRequestService;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.ResponseCode;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.qcri.micromappers.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jlucas on 2/10/17.
 */
@Controller
@RequestMapping("/service/request")
public class ServiceRequestController {
    private static Logger logger = Logger.getLogger(ServiceRequestController.class);

    @Autowired
    ComputerVisionRequestService computerVisionRequestService;

    @Autowired
    private Util util;


    @RequestMapping(value = "/cv", method = RequestMethod.GET)
    @ResponseBody
    public ResponseWrapper addComputerVisionRequst(@RequestParam("id") Long id,
                                           @RequestParam("type") String type)
            throws Exception {

        String msg = "Error while adding cv request.";
        Account user = util.getAuthenticatedUser();

        try{
            if (id == null || user == null){
                return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), msg);
            }

            ComputerVisionRequest computerVisionRequest = this.loadComputerVisionRequest(type,id, user.getId());

            if(computerVisionRequest != null){
                ComputerVisionRequest newComputerVisionRequest = computerVisionRequestService.saveOrUpdate(computerVisionRequest);

                if(newComputerVisionRequest != null) {
                    return new ResponseWrapper(null, true, ResponseCode.SUCCESS.toString(), null);
                }
                return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), msg);
            }
            else{
                return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), msg);
            }

        }catch(Exception e){
            logger.error(msg, e);
            return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), msg);
        }

    }

    private ComputerVisionRequest loadComputerVisionRequest (String type, long id, long accountId){

        ComputerVisionRequest computerVisionRequest = null;
        if(type.equalsIgnoreCase("datafeed")){
            computerVisionRequest =
                    new ComputerVisionRequest(Constants.COMPUTER_VISION_ON_REQUEST);
            computerVisionRequest.setDataFeedId(id);

        }
        if(type.equalsIgnoreCase("gdelt")){
            computerVisionRequest =
                    new ComputerVisionRequest(Constants.COMPUTER_VISION_ON_REQUEST);
            computerVisionRequest.setGlideMasterId(id);
        }
        if(type.equalsIgnoreCase("collection")){
            computerVisionRequest =
                    new ComputerVisionRequest(Constants.COMPUTER_VISION_ON_REQUEST);
            computerVisionRequest.setCollectionId(id);

        }

        return computerVisionRequest;
    }


}

