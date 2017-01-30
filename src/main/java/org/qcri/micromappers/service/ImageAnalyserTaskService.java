package org.qcri.micromappers.service;

import org.apache.log4j.Logger;
import org.qcri.micromappers.classifier.image.MSCognitiveClassifier;
import org.qcri.micromappers.classifier.image.MSCognitiveProcessor;
import org.qcri.micromappers.entity.*;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.ImageAnalyserTaskRepository;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.ComputerVisionStatus;
import org.qcri.micromappers.utility.Util;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.springframework.stereotype.Service;
import twitter4j.JSONObject;

import javax.inject.Inject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by jlucas on 1/25/17.
 */
@Service
public class ImageAnalyserTaskService {
    private static Logger logger = Logger.getLogger(GlobalEventDefinitionService.class);
    private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();


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
            ImageAnalysis analysis = MSCognitiveProcessor.processClassifiedInfo(response, taskRecord, imageURL);
            if(analysis!=null){
                imageAnalysisService.createImageAnalysis(analysis);
                imageAnalyserTask.setState(ComputerVisionStatus.COMPUTER_VISION_ANALYSER_TASK_COMPLETED.getStatus());
                saveOrUpdate(imageAnalyserTask);
            }
        }
        else{
            imageAnalyserTask.setState(ComputerVisionStatus.COMPUTER_VISION_ANALYSER_TASK_COMPLETED.getStatus());
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
            DataFeed dataFeed = (DataFeed)taskRecord;
            String feed_id = dataFeed.getFeedId();
            Path parentPath = Paths.get(configProperties.getProperty(MicromappersConfigurationProperty.Feed_PATH),
                    dataFeed.getCollection().getCode(), dataFeed.getProvider().toString());

            Path filePath = Paths.get(parentPath.toString(), dataFeed.getFeedId());
            String fileContent = Util.getFileContent(filePath);
            if(fileContent != null && !fileContent.isEmpty() && dataFeed.getProvider().equals(CollectionType.TWITTER)){
                String imageUrl = getJsonTwitterImageURL(fileContent);
                return imageUrl;
            }
        }

        return null;
    }

    private String getJsonTwitterImageURL(String message) {
        String imageUrl = null;
        try{
            JSONObject msgJson  = new JSONObject(message);

            JSONObject entities = msgJson.getJSONObject("entities");
            if(entities != null && entities.has("media")
                    && entities.getJSONArray("media") != null
                    && entities.getJSONArray("media").length() > 0
                    && entities.getJSONArray("media").getJSONObject(0).getString("type") != null
                    && entities.getJSONArray("media").getJSONObject(0).getString("type").equals("photo"))
            {

                imageUrl = entities.getJSONArray("media").getJSONObject(0).getString("media_url");

            }

        }catch(Exception e){
            logger.error("Error in getJsonMediaImageURL :::: " + message);
        }

        return imageUrl;
    }
}
