package org.qcri.micromappers.service;

import org.apache.log4j.Logger;
import org.qcri.micromappers.classifier.image.MSCognitiveClassifier;
import org.qcri.micromappers.classifier.image.MSCognitiveProcessor;
import org.qcri.micromappers.entity.*;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.ImageAnalyserTaskRepository;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.ComputerVisionStatus;
import org.qcri.micromappers.utility.HttpDownloadUtility;
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

    @Inject
    private Util util;

    public List<ImageAnalyserTask> findByState(String state){
        List<ImageAnalyserTask> tasks = null;
        if(state.equalsIgnoreCase(ComputerVisionStatus.COMPUTER_VISION_ANALYSER_TASK_ONGOING.getStatus())){
            tasks = imageAnalyserTaskRepository.findByState(state);
        }
        return tasks;
    }

    public ImageAnalyserTask saveOrUpdate(ImageAnalyserTask imageAnalyserTask){
        try{
            if(!this.isRecordExist(imageAnalyserTask)){
                return imageAnalyserTaskRepository.save(imageAnalyserTask);
            }
            else{
                //throw new MicromappersServiceException("Exception while create or update a ImageAnalyserTask : duplicate found");
                logger.warn("Exception while create or update a ImageAnalyserTask : duplicate found" );
                return null;
            }

        }catch (Exception e) {
            logger.error("Error while create or update ImageAnalyserTask", e);
            throw new MicromappersServiceException("Exception while create or update a ImageAnalyserTask", e);
        }
    }

    public void runImageAnalyserTask(Object taskRecord, ImageAnalyserTask imageAnalyserTask){

        String imageURL = this.getImageURL(taskRecord);

        if(imageURL != null){
            if(HttpDownloadUtility.isExist(imageURL) && !this.isProcessed(imageURL)){
                String response = MSCognitiveClassifier.analyzeImage(imageURL);
                //String response = MSCognitiveProcessor.getTempJson();
                this.persistToFile(imageAnalyserTask.getId().toString(), response);
                ImageAnalysis analysis = MSCognitiveProcessor.processClassifiedInfo(response, taskRecord, imageURL);
                if(analysis!=null){
                    imageAnalysisService.createImageAnalysis(analysis);
                    imageAnalyserTask.setState(ComputerVisionStatus.COMPUTER_VISION_ANALYSER_TASK_COMPLETED.getStatus());
                    saveOrUpdate(imageAnalyserTask);
                }
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

    private boolean isProcessed(String imgURL){
        ImageAnalysis imageAnalysis = imageAnalysisService.findByImgURL(imgURL);
        if(imageAnalysis != null){
            return true;
        }
        return false;
    }

    private void persistToFile(String fileName, String feed ){
        Path parentPath = Paths.get(configProperties.getProperty(MicromappersConfigurationProperty.IMAGE_CLASSIFIER_PATH));

        if(util.createDirectories(parentPath)){
            Path filePath = Paths.get(parentPath.toString(), fileName);
            util.writeToFile(filePath, feed);
        }
    }

    private boolean isRecordExist(ImageAnalyserTask imageAnalyserTask){
        if(imageAnalyserTask.getGdelt3W() != null){
            List<ImageAnalyserTask> task1 = imageAnalyserTaskRepository.findByGdelt3W(imageAnalyserTask.getGdelt3W());
            List<ImageAnalyserTask> img1 = imageAnalyserTaskRepository.findByImageURL(imageAnalyserTask.getGdelt3W().getImgURL());
            if(task1 != null && !task1.isEmpty() && !img1.isEmpty()){
                logger.warn("Gdelt3W duplicate found : " + imageAnalyserTask.getGdelt3W().getId());
                return true;
            }
        }

        if(imageAnalyserTask.getGdeltMMIC() != null){
            List<ImageAnalyserTask> task2 = imageAnalyserTaskRepository.findByGdeltMMIC(imageAnalyserTask.getGdeltMMIC());
            List<ImageAnalyserTask> img2 = imageAnalyserTaskRepository.findByImageURL(imageAnalyserTask.getGdeltMMIC().getImgURL());
            if(task2 != null && !task2.isEmpty()  && !img2.isEmpty()){
                logger.warn("Gdeltmmic duplicate found : " + imageAnalyserTask.getGdeltMMIC().getId());
                return true;
            }
        }

        if(imageAnalyserTask.getDataFeed() != null){
            List<ImageAnalyserTask> task3 = imageAnalyserTaskRepository.findByDataFeed(imageAnalyserTask.getDataFeed());
            if(task3 != null && !task3.isEmpty()){
                logger.warn("datafeed duplicate found : " + imageAnalyserTask.getDataFeed().getId());
                return true;
            }
        }

        return false;

    }
}
