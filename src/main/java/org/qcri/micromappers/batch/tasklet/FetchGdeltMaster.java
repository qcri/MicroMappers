package org.qcri.micromappers.batch.tasklet;


import org.qcri.micromappers.model.GdeltMaster;
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
    private String MASTER_URL = "http://data.gdeltproject.org/micromappers/lastupdate.txt";
    private String MASTER_LOC = "/Users/jlucas/Documents/JavaDev/data/resources";
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Execution ***************************");
        URL url = new URL(MASTER_URL);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null){
            GdeltMaster gdeltMaster = new GdeltMaster(inputLine);
            HttpDownloadUtility httpDownloadUtility = new HttpDownloadUtility();
            httpDownloadUtility.downloadFile(gdeltMaster.getMmURL(), MASTER_LOC);
            System.out.println(inputLine);
        }

        in.close();

        System.out.println("Execution Finish***************************");




        return RepeatStatus.FINISHED;
    }
}
