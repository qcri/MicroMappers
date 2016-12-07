package org.qcri.micromappers.batch.processor;


import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GdeltMMIC;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by jlucas on 11/29/16.
 */
public class GdeltMMICProcessor implements ItemProcessor<GdeltMMIC, GdeltMMIC> {

    private static Logger logger = Logger.getLogger(GdeltMMICProcessor.class);

    @Override
    public GdeltMMIC process(GdeltMMIC gdeltMMIC) throws Exception {

        try{
            gdeltMMIC.setGlideCode(gdeltMMIC.getGlideCode().replace(";",","));
        }
        catch(Exception e){
            logger.error(e.getMessage());
        }

        return gdeltMMIC;

    }
}
