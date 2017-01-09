package org.qcri.micromappers.batch.tasklet;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.qcri.micromappers.models.GdeltMaster;
import org.qcri.micromappers.utility.HttpDownloadUtility;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Created by jlucas on 11/28/16.
 */
public class FetchGdeltMaster implements Tasklet {
    private static Logger logger = Logger.getLogger(FetchGdeltMaster.class);

    private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();
	
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        URL url = new URL(configProperties.getProperty(MicromappersConfigurationProperty.GDELT_LAST_UPDATE_URL));
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null){
            GdeltMaster gdeltMaster = new GdeltMaster(inputLine);
            HttpDownloadUtility.downloadFile(gdeltMaster.getMmURL(), configProperties.getProperty(MicromappersConfigurationProperty.GDELT_DOWNLOADED_LAST_UPDATE_PATH), null);
        }

        in.close();

        this.reformat3WJason();

        return RepeatStatus.FINISHED;
    }

    private void reformat3WJason() throws IOException {

        List<String> result = Files.find(Paths.get(configProperties.getProperty(MicromappersConfigurationProperty.GDELT_DOWNLOADED_LAST_UPDATE_PATH)), 1,
                (p, a) -> p.toString().toLowerCase().endsWith("mm3w.json"))
                .map(path -> path.toString())
                .collect(Collectors.toList());

        JSONParser parser = new JSONParser();

        result.forEach(item -> {
            File file = new File(item);
            JSONObject jsonContext = null;
            try {
                jsonContext = (JSONObject)parser.parse(FileUtils.readFileToString(file));

                if(!jsonContext.isEmpty() && jsonContext.containsKey("data")){
                    this.generateJsonFile((JSONArray)jsonContext.get("data"), file);
                }

                File newMaserFile = new File(file.getAbsolutePath() + ".processed");
                FileUtils.copyFile(file, newMaserFile);
                file.delete();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e2){
                e2.printStackTrace();
            }


        });
    }

    private void generateJsonFile(JSONArray gdelt3WArrary, File file){

        int index = 0;
        try{
            for(Object o : gdelt3WArrary){
                JSONObject jsonObject = (JSONObject)o;

                File newfile = new File(configProperties.getProperty(MicromappersConfigurationProperty.GDELT_JSON_UPDATE_PATH)+ File.separator+ file.getName()+"_"+index+".json");

                FileUtils.writeStringToFile(newfile,jsonObject.toJSONString());
                index++;
            }

        }catch (IOException e) {
            logger.error("generateJsonFile IOException: " + e);
        }
        catch (Exception e2) {
            logger.error("generateJsonFile Exception: " + e2);
        }

    }
}
