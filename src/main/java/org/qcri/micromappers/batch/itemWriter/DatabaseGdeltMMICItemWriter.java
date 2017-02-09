package org.qcri.micromappers.batch.itemWriter;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Gdelt3W;
import org.qcri.micromappers.entity.GdeltMMIC;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.service.Gdelt3WService;
import org.qcri.micromappers.service.GdeltMMICService;
import org.qcri.micromappers.service.GlideMasterService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by jlucas on 1/31/17.
 */

public class DatabaseGdeltMMICItemWriter implements ItemWriter<GdeltMMIC>{

    private static Logger logger = Logger.getLogger(DatabaseGdeltMMICItemWriter.class);

    @Autowired
    GdeltMMICService gdeltMMICService;

    @Autowired
    GlideMasterService glideMasterService;

    @Override
    public void write(List<? extends GdeltMMIC> list) throws Exception {
        list.forEach(item->{
            this.processOneRecord(item);
        });


    }

    public void processOneRecord(GdeltMMIC gdeltMMIC){
        String[] code = gdeltMMIC.getGlideCode().split(",");

        for(int i =0; i < code.length; i++){
            GdeltMMIC temp = new GdeltMMIC(gdeltMMIC.getLanguageCode(), gdeltMMIC.getArticleURL(),
                    gdeltMMIC.getTimestamp(), gdeltMMIC.getLocation(),
                    gdeltMMIC.getLat(), gdeltMMIC.getLon(), gdeltMMIC.getImgURL(), code[i].trim());

            temp.setComputerVisionEnabled(this.isComputerVisionRequested(code[i].trim()));

            gdeltMMICService.saveOrUpdate(temp);
        }
    }

    private boolean isComputerVisionRequested(String glideCode){

        GlideMaster glideMaster = glideMasterService.getByGlideCode(glideCode);

        if(glideMaster != null){
            return glideMaster.getComputerVisionEnabled();
        }

        return false;
    }
}
