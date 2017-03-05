package org.qcri.micromappers.service;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GdeltMMIC;
import org.qcri.micromappers.entity.ImageAnalysis;
import org.qcri.micromappers.repository.ImageAnalysisRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Created by jlucas on 1/24/17.
 */
@Service
public class ImageAnalysisService {
    private static Logger logger = Logger.getLogger(ImageAnalysisService.class);

    @Inject
    private ImageAnalysisRepository imageAnalysisRepository;

    @Transactional
    public void createImageAnalysis(ImageAnalysis imageAnalysis){
        try{
            imageAnalysisRepository.save(imageAnalysis);
        }
        catch(Exception e){
            logger.error(e);
        }

    }

    public ImageAnalysis findByImgURL(String imgURL){
        return imageAnalysisRepository.findByImageURL(imgURL);
    }


}
