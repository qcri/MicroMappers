package org.qcri.micromappers.batch.tasklet;


import org.qcri.micromappers.entity.GdeltMaster;
import org.qcri.micromappers.utility.FilePathSpec;
import org.qcri.micromappers.utility.HttpDownloadUtility;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by jlucas on 11/28/16.
 */
public class FetchGdeltMaster implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Execution ***************************");
        URL url = new URL(FilePathSpec.GDELT_LAST_UPDATE_URL);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null){
            GdeltMaster gdeltMaster = new GdeltMaster(inputLine);
           // HttpDownloadUtility httpDownloadUtility = new HttpDownloadUtility();
            HttpDownloadUtility.downloadFile(gdeltMaster.getMmURL(), FilePathSpec.GDELT_DOWNLOADED_LAST_UPDATE_PATH, null);
            System.out.println(inputLine);
        }

        in.close();

        System.out.println("Execution Finish***************************");




        return RepeatStatus.FINISHED;
    }
}
