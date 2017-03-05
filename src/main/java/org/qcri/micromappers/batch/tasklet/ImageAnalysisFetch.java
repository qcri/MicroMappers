package org.qcri.micromappers.batch.tasklet;

import org.apache.log4j.Logger;
import org.qcri.micromappers.classifier.image.MSCognitiveClassifier;
import org.qcri.micromappers.entity.ImageAnalyserTask;
import org.qcri.micromappers.service.ImageAnalyserTaskService;
import org.qcri.micromappers.utility.ComputerVisionStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by jlucas on 1/24/17.
 */
public class ImageAnalysisFetch implements Tasklet {

   private static Logger logger = Logger.getLogger(ImageAnalysisFetch.class);
   private MSCognitiveClassifier msCognitiveClassifier = new MSCognitiveClassifier();

    @Autowired
    ImageAnalyserTaskService imageAnalyserTaskService;

   @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
       /**
        * 1. get dataset for image classifcation
        * 2. get data result from msCognitiveClassifier.analyzeImage
        * 3. process & save the result
        */
       List<ImageAnalyserTask> taskList = imageAnalyserTaskService.findByState(ComputerVisionStatus.COMPUTER_VISION_ANALYSER_TASK_ONGOING.getStatus());

       if(taskList.isEmpty()){
           return RepeatStatus.FINISHED;
       }

       taskList.forEach(item -> {
           boolean processed = false;
           if(item.getGdelt3W() != null && !processed){
                //process 3w
               imageAnalyserTaskService.runImageAnalyserTask(item.getGdelt3W(), item);
               processed = true;
           }

           if(item.getGdeltMMIC() != null && !processed){
               // process mmic
               imageAnalyserTaskService.runImageAnalyserTask(item.getGdeltMMIC(), item);
               processed = true;
           }

           if(item.getDataFeed() != null && !processed){
               // process twitter & facebook
               imageAnalyserTaskService.runImageAnalyserTask(item.getDataFeed(), item);
               processed = true;
           }
       });

       return RepeatStatus.FINISHED;
    }

}
