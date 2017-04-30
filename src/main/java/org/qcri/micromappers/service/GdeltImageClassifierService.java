package org.qcri.micromappers.service;

import org.apache.commons.collections.IteratorUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.GdeltImageClassifier;
import org.qcri.micromappers.repository.GdeltImageClassifierRepository;
import org.qcri.micromappers.utility.Constants;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by jlucas on 4/30/17.
 */
@Service
public class GdeltImageClassifierService {
    private static Logger logger = Logger.getLogger(GdeltImageClassifierService.class);

    @Inject
    private GdeltImageClassifierRepository gdeltImageClassifierRepository;

    public void saveOrUpdate(GdeltImageClassifier gdeltImageClassifier){

        gdeltImageClassifierRepository.save(gdeltImageClassifier);

    }

    public void saveOrUpdate(String data, Account account){

        GdeltImageClassifier gdeltImageClassifier = this.getGdeltImageClassifier(data, account);

        if(gdeltImageClassifier != null){
            gdeltImageClassifierRepository.save(gdeltImageClassifier);
        }
    }

    public List<GdeltImageClassifier> getAllByState(String state){

        return gdeltImageClassifierRepository.findByState(state);
    }

    public List<GdeltImageClassifier> getAll(){

        return (List<GdeltImageClassifier>)gdeltImageClassifierRepository.findAll();
    }

    private GdeltImageClassifier getGdeltImageClassifier(String data, Account account){
        try{
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(data);
            String name = jsonObject.get("name").toString();
            String theme= jsonObject.get("theme").toString();
            String location= jsonObject.get("loc").toString();
            String locationCC= jsonObject.get("cc").toString();
            String imageWebTag= jsonObject.get("webtag").toString();
            String imageTag= jsonObject.get("tag").toString();

            return new GdeltImageClassifier(name, theme, location, locationCC, imageWebTag, imageTag, account, Constants.GENERAL_ACTIVE);
        }
        catch(Exception e){
            logger.error(e);
            return null;
        }
    }
}
