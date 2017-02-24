package org.qcri.micromappers.batch.mapper;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.qcri.micromappers.entity.Gdelt3W;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.JsonLineMapper;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by jlucas on 12/4/16.
 */
public class Gdelt3WFieldSetMapper implements LineMapper<Gdelt3W> {
    private JsonLineMapper delegate;
    private JSONParser parser = new JSONParser();

    @Override
    public Gdelt3W mapLine(String line, int lineNumber) throws Exception {
        Map<String, Object> gdelt3wAsMap = delegate.mapLine(line, lineNumber);

        Gdelt3W gdelt3W = null;

        if(gdelt3wAsMap.keySet().contains("imagelink")){
            String imageLink = gdelt3wAsMap.get("imagelink").toString();

            int httpIndex = imageLink.lastIndexOf("http://");
            int httpsIndex = imageLink.lastIndexOf("https://");

            if(httpIndex > 0 || httpsIndex > 0){
                if(httpIndex > 0) {
                    imageLink = imageLink.substring(imageLink.lastIndexOf("httpIndex")) ;
                }
                else{
                    imageLink = imageLink.substring(imageLink.lastIndexOf("httpsIndex")) ;
                }
            }

            gdelt3W = new Gdelt3W(gdelt3wAsMap.get("langcode").toString(),
                    gdelt3wAsMap.get("link").toString(),
                    imageLink,
                    gdelt3wAsMap.get("glide").toString().replace("[","").replace("]",""),
                    "insert");
        }
        else{
            gdelt3W = new Gdelt3W(gdelt3wAsMap.get("langcode").toString(),
                    gdelt3wAsMap.get("link").toString(),
                    gdelt3wAsMap.get("glide").toString().replace("[","").replace("]",""),
                    "insert");
        }

        gdelt3W.setWheres(this.buildWhereMap(gdelt3wAsMap));
        gdelt3W.setWho(this.buildWhoMap(gdelt3wAsMap));

        return gdelt3W;
    }

    private String buildWhereMap(Map<String, Object> gdelt3wAsMap){
        String jsonString = null;
        if(gdelt3wAsMap.keySet().contains("where")){
            ArrayList whereList = (ArrayList)gdelt3wAsMap.get("where");
            if(!whereList.isEmpty()){
                jsonString = JSONArray.toJSONString(whereList);
            }
        }

        return jsonString;
    }

    private String buildWhoMap(Map<String, Object> gdelt3wAsMap){
        String jsonString = null;
        if(gdelt3wAsMap.keySet().contains("who")){
            ArrayList whoList = (ArrayList)gdelt3wAsMap.get("who");
            if(!whoList.isEmpty()){
                StringBuilder b = new StringBuilder();
                whoList.forEach(b::append);
                jsonString = b.toString();
            }
        }
        return jsonString;
    }

    public void setDelegate(JsonLineMapper delegate) {
        this.delegate = delegate;
    }

}
