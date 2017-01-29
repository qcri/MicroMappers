package org.qcri.micromappers.service;

import org.apache.log4j.Logger;
import org.qcri.micromappers.classifier.image.MSCognitiveClassifier;
import org.qcri.micromappers.classifier.image.MSCognitiveProcessor;
import org.qcri.micromappers.entity.*;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.ImageAnalyserTaskRepository;
import org.qcri.micromappers.utility.ComputerVisionStatus;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by jlucas on 1/25/17.
 */
@Service
public class ImageAnalyserTaskService {
    private static Logger logger = Logger.getLogger(GlobalEventDefinitionService.class);

    @Inject
    private ImageAnalyserTaskRepository imageAnalyserTaskRepository;

    @Inject
    private ImageAnalysisService imageAnalysisService;

    public List<ImageAnalyserTask> findByState(String state){
        List<ImageAnalyserTask> tasks = null;
        if(state.equalsIgnoreCase(ComputerVisionStatus.COMPUTER_VISION_ANALYSER_TASK_ONGOING.getStatus())){
            tasks = imageAnalyserTaskRepository.findByState(state);
        }
        return tasks;
    }

    public ImageAnalyserTask saveOrUpdate(ImageAnalyserTask imageAnalyserTask){
        try{
            return imageAnalyserTaskRepository.save(imageAnalyserTask);
        }catch (Exception e) {
            logger.error("Error while create or update ImageAnalyserTask", e);
            throw new MicromappersServiceException("Exception while create or update a ImageAnalyserTask", e);
        }
    }

    public void runImageAnalyserTask(Object taskRecord, ImageAnalyserTask imageAnalyserTask){

        String imageURL = this.getImageURL(taskRecord);

        if(imageURL != null){
            String response = MSCognitiveClassifier.analyzeImage(imageURL);
            //String response = MSCognitiveProcessor.getTempJson();
            ImageAnalysis analysis = MSCognitiveProcessor.processClassifiedInfo(response, taskRecord);
            if(analysis!=null){
                imageAnalysisService.createImageAnalysis(analysis);
                imageAnalyserTask.setState(ComputerVisionStatus.COMPUTER_VISION_ANALYSER_TASK_COMPLETED.getStatus());
                saveOrUpdate(imageAnalyserTask);
            }

        }

    }

    private String getImageURL(Object taskRecord){
        if(taskRecord instanceof Gdelt3W){
            Gdelt3W gdelt3W = (Gdelt3W)taskRecord;
            return  gdelt3W.getImgURL();
        }

        if(taskRecord instanceof GdeltMMIC){
            GdeltMMIC gdeltMMIC = (GdeltMMIC)taskRecord;
            return  gdeltMMIC.getImgURL();
        }

        if(taskRecord instanceof DataFeed){
            // LET SEE
        }


        return null;
    }

}
