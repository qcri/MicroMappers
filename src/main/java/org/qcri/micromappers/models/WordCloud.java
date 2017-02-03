package org.qcri.micromappers.models;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by jlucas on 1/19/17.
 */
public class WordCloud {
    private String text;
    private long size;

    public WordCloud() {
    }

    public WordCloud(String text, long size) {
        this.text = text;
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @JsonIgnore
    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", this.text);
       // this.size = (long) (10 + (Math.random() * 90));
        jsonObject.put("size", this.size);

        return jsonObject;
    }
}
