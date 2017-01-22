package org.qcri.micromappers.utility;

/**
 * Created by jlucas on 1/16/17.
 */
public enum GlobalDataSourceType {
    SNOPES("snopes"),
    GDELT("gdelt"),
    NEWS("news"),
    GDELT3W("gdlet3w"),
    GDELTMMIC("gdeltmmic");

    private String value;

    private GlobalDataSourceType(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
