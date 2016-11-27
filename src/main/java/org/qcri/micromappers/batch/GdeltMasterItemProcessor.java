package org.qcri.micromappers.batch;

import org.qcri.micromappers.model.GdeltMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class GdeltMasterItemProcessor implements ItemProcessor<GdeltMaster, GdeltMaster> {

    private static final Logger log = LoggerFactory.getLogger(GdeltMasterItemProcessor.class);

    @Override
    public GdeltMaster process(final GdeltMaster gdeltMaster) throws Exception {
        GdeltMaster transformedGdeltMaster = null;
        try{
            final String mmURL =gdeltMaster.getMmURL();

            transformedGdeltMaster = new GdeltMaster(mmURL);

            log.info("Converting (" + gdeltMaster + ") into (" + transformedGdeltMaster + ")");

        }
        catch(Exception e){
            log.error(e.getMessage());
        }

        return transformedGdeltMaster;
    }

}
