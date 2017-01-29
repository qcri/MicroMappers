package org.qcri.micromappers.classifier.image;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.qcri.micromappers.entity.*;
import org.qcri.micromappers.utility.ComputerVisionStatus;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jlucas on 1/25/17.
 */
public class MSCognitiveProcessor {

    private static Logger logger = Logger.getLogger(MSCognitiveProcessor.class);

    public static ImageAnalysis processClassifiedInfo(String classfiedInfo, Object dataObject){
        ImageAnalysis analysis = null;
        if(ComputerVisionStatus.EXCEPTION_NOT_CORRECT_IMAGE_FORMAT.getStatus().equalsIgnoreCase(classfiedInfo)){
            return analysis;
        }
        if(ComputerVisionStatus.EXCEPTION_NOT_VALID_IMAGE_URL.getStatus().equalsIgnoreCase(classfiedInfo)){
            return analysis;
        }
        if(ComputerVisionStatus.EXCEPTION_UNKNOWN.getStatus().equalsIgnoreCase(classfiedInfo)){
            return analysis;
        }

        try{
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(classfiedInfo)  ;
            ImageAdult adult = generateAdult((JSONObject)jsonObject.get("adult"));

            Set<ImageClassify> classifies = generateCategory((JSONArray)jsonObject.get("categories"));
            Set<ImageTag> tags = generateTag((JSONArray)jsonObject.get("tags"));
            Set<ImageDescription> descriptionSet = generateDescription((JSONObject) jsonObject.get("description"));

            analysis = createImageAnalysisInstance(dataObject, adult,classifies,tags,descriptionSet);

            adult.setImageAnalysis(analysis);
            classifies = setImageAnalysisForCategory(classifies, analysis);
            tags = setImageAnalysisForTag(tags, analysis);
            descriptionSet = setImageAnalysisForDesc(descriptionSet, analysis);

        }
        catch (Exception e){
            logger.error(e);
        }

        return analysis;
    }

    private static ImageAnalysis createImageAnalysisInstance(Object dataObject,ImageAdult adult,Set<ImageClassify> classifies,
                                                      Set<ImageTag> tags, Set<ImageDescription> descriptionSet){
        ImageAnalysis analysis = null;
        if(dataObject instanceof GdeltMMIC) {
            GdeltMMIC gdeltMMIC = (GdeltMMIC)dataObject;
            analysis = new ImageAnalysis(gdeltMMIC.getImgURL(), null, gdeltMMIC,
                    ComputerVisionStatus.OK.getStatus(), adult, descriptionSet, classifies,
                    tags,null
            );
        }
        if(dataObject instanceof Gdelt3W){
            Gdelt3W gdelt3W = (Gdelt3W)dataObject;
            analysis = new ImageAnalysis(gdelt3W.getImgURL(), gdelt3W, null,
                    ComputerVisionStatus.OK.getStatus(), adult, descriptionSet, classifies,
                    tags,null
            );

        }
        if(dataObject instanceof DataFeed){
            // let's see how we will handle when we get dataset here.
        }

        return analysis;
    }

    private static Set<ImageClassify> setImageAnalysisForCategory(Set<ImageClassify> categories, ImageAnalysis analysis){
        categories.forEach(item->{
            item.setImageAnalysis(analysis);
        });

        return categories;
    }

    private static Set<ImageTag> setImageAnalysisForTag(Set<ImageTag> tags, ImageAnalysis analysis){
        tags.forEach(item->{
            item.setImageAnalysis(analysis);
        });

        return tags;
    }

    private static Set<ImageDescription> setImageAnalysisForDesc(Set<ImageDescription> descriptionSet, ImageAnalysis analysis){
        descriptionSet.forEach(item->{
            item.setImageAnalysis(analysis);
        });

        return descriptionSet;
    }

    private static Set<ImageTag> generateTag(JSONArray jsonArray){
        Set<ImageTag> tagSet = new HashSet<ImageTag>();
        for(Object value : jsonArray){
            JSONObject obj = (JSONObject)value;
            ImageTag temp = new ImageTag(obj.get("name").toString(),
                    BigDecimal.valueOf(Double.parseDouble(obj.get("confidence").toString()))
            );
            tagSet.add(temp);
        }

        return tagSet;
    }

    private static Set<ImageClassify> generateCategory(JSONArray jsonArray){
        Set<ImageClassify> categorySet = new HashSet<ImageClassify>();
        for(Object value : jsonArray){
            JSONObject obj = (JSONObject)value;
            ImageClassify temp = new ImageClassify(obj.get("name").toString(),
                    BigDecimal.valueOf(Double.parseDouble(obj.get("score").toString()))
            );
            categorySet.add(temp);
        }

        return categorySet;
    }

    private static ImageAdult generateAdult(JSONObject jsonObject){
        Boolean isAdultContent = Boolean.parseBoolean(jsonObject.get("isAdultContent").toString());
        Boolean isRacyContent = Boolean.parseBoolean(jsonObject.get("isRacyContent").toString());
        BigDecimal adultScore = BigDecimal.valueOf(Double.parseDouble(jsonObject.get("adultScore").toString()));
        BigDecimal racyScore = BigDecimal.valueOf(Double.parseDouble(jsonObject.get("racyScore").toString()));
        ImageAdult imageAdult = new ImageAdult(isAdultContent,isRacyContent,adultScore,racyScore);

        return imageAdult;
    }

    private static Set<ImageDescription> generateDescription(JSONObject jsonObject){
        Set<ImageDescription> descriptionSet = new HashSet<ImageDescription>();

        JSONArray tags = (JSONArray)jsonObject.get("tags");
        StringBuffer stringBuffer = new StringBuffer();
        for(Object tag : tags){
            stringBuffer.append(tag.toString());
            stringBuffer.append(",");
        }

        JSONArray captions = (JSONArray)jsonObject.get("captions");
        for(Object caption : captions){
            JSONObject obj = (JSONObject)caption;
            String text = obj.get("text").toString();
            BigDecimal confidence = BigDecimal.valueOf(Double.parseDouble(obj.get("confidence").toString()));
            descriptionSet.add(new ImageDescription(stringBuffer.toString(), text, confidence));
        }

        return descriptionSet;
    }

    // this is for testing purpose only
    public static String getTempJson(){
        StringBuffer buffer = new StringBuffer();
        try{
            String filePath = new ClassPathResource("sampleJson.txt").getFile().getAbsolutePath();
            List<String> list= new ArrayList<>();

            try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath), Charset.forName("UTF-8")))
            {
                list = br.lines().collect(Collectors.toList());

                for(String a : list){
                    buffer.append(a);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e){
            logger.error(e);
        }

        return buffer.toString();
    }

}
