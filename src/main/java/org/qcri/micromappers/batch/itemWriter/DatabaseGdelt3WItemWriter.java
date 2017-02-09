package org.qcri.micromappers.batch.itemWriter;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Gdelt3W;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.service.Gdelt3WService;
import org.qcri.micromappers.service.GlideMasterService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by jlucas on 1/31/17.
 */

public class DatabaseGdelt3WItemWriter implements ItemWriter<Gdelt3W>{

    private static Logger logger = Logger.getLogger(DatabaseGdelt3WItemWriter.class);

    @Autowired
    Gdelt3WService gdelt3WService;

    @Autowired
    GlideMasterService glideMasterService;

    @Override
    public void write(List<? extends Gdelt3W> list) throws Exception {


        list.forEach(item->{
            this.processOneRecord(item);
        });


    }

    public void processOneRecord(Gdelt3W gdelt3W){
        String[] code = gdelt3W.getGlideCode().split(",");
        gdelt3W.setState("insert");

        for(int i =0; i < code.length; i++){
            Gdelt3W temp = new Gdelt3W(gdelt3W.getLanguageCode(),gdelt3W.getArticleURL(),gdelt3W.getImgURL(),
                    code[i].trim(),gdelt3W.getState());
            temp.setComputerVisionEnabled(isComputerVisionRequested(code[i].trim()));
            gdelt3WService.saveOrUpdate(temp);
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
