package org.qcri.micromappers.models;

/**
 * Created by jlucas on 11/26/16.
 */
public class GdeltMaster {

    private String mmType;
    private String mmURL;


    public GdeltMaster() {
    }

    public GdeltMaster(String mmURL) {
        this.mmURL = mmURL;
        this.resetmmType(mmURL);
    }

    public GdeltMaster(String mmURL, String mmType) {
        if(mmType == null){
            this.resetmmType(mmURL);
        }
        else{
            this.mmType = mmType;
        }

        this.mmURL = mmURL;
    }

    public void resetmmType(String mmURL){
        if(mmURL.contains("mmic")){
            this.mmType = "mmic";
        }

        if(mmURL.contains("mm3w")){
            this.mmType = "mm3w";
        }

        if(mmURL.contains("gkgglide")){
            this.mmType = "gkgglide";
        }
    }

    public String getMmType() {
        return mmType;
    }

    public void setMmType(String mmType) {
        this.mmType = mmType;
    }

    public String getMmURL() {
        return mmURL;
    }

    public void setMmURL(String mmURL) {
        this.mmURL = mmURL;
    }

    @Override
    public String toString() {

        return "mmURL: " + mmURL + ", mmType: " + mmType;
    }

}
