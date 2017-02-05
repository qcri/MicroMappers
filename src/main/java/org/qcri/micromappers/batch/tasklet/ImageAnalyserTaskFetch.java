package org.qcri.micromappers.batch.tasklet;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.DataFeed;
import org.qcri.micromappers.entity.Gdelt3W;
import org.qcri.micromappers.entity.GdeltMMIC;
import org.qcri.micromappers.entity.ImageAnalyserTask;
import org.qcri.micromappers.service.DataFeedService;
import org.qcri.micromappers.service.Gdelt3WService;
import org.qcri.micromappers.service.GdeltMMICService;
import org.qcri.micromappers.service.ImageAnalyserTaskService;
import org.qcri.micromappers.utility.ComputerVisionStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by jlucas on 1/29/17.
 */
public class ImageAnalyserTaskFetch implements Tasklet {

    private static Logger logger = Logger.getLogger(ImageAnalyserTaskFetch.class);

    @Autowired
    DataFeedService dataFeedService;

    @Autowired
    ImageAnalyserTaskService imageAnalyserTaskService;

    @Autowired
    GdeltMMICService gdeltMMICService;

    @Autowired
    Gdelt3WService gdelt3WService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        List<DataFeed> dataFeedList = dataFeedService.findByComputerVisionEnabled(true);

        dataFeedList.forEach(item -> {
            if(item.getImageAnalyserTask() == null){
                ImageAnalyserTask analyserTask = new ImageAnalyserTask(ComputerVisionStatus.COMPUTER_VISION_ANALYSER_TASK_ONGOING.getStatus(),
                        item.getCollection(), item);

                imageAnalyserTaskService.saveOrUpdate(analyserTask);
            }

        });

        List<GdeltMMIC> gdeltMMICList = gdeltMMICService.findByComputerVisionEnabled(true);

        gdeltMMICList.forEach(item -> {
            if(item.getImageAnalyserTask() == null){
                ImageAnalyserTask analyserTask = new ImageAnalyserTask(ComputerVisionStatus.COMPUTER_VISION_ANALYSER_TASK_ONGOING.getStatus(),
                        item);

                imageAnalyserTaskService.saveOrUpdate(analyserTask);
            }

        });

        List<Gdelt3W> gdelt3WList = gdelt3WService.findByComputerVisionEnabled(true);

        gdelt3WList.forEach(item -> {
            if(item.getImageAnalyserTask() == null){
                ImageAnalyserTask analyserTask = new ImageAnalyserTask(ComputerVisionStatus.COMPUTER_VISION_ANALYSER_TASK_ONGOING.getStatus(),
                        item);

                imageAnalyserTaskService.saveOrUpdate(analyserTask);
            }

        });

        return RepeatStatus.FINISHED;
    }
}
