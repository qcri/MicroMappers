package org.qcri.micromappers.batch.processor;


import org.qcri.micromappers.entity.Gdelt3W;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by jlucas on 12/4/16.
 */
public class Gdelt3WProcessor implements ItemProcessor<Gdelt3W, Gdelt3W> {
    @Override
    public Gdelt3W process(Gdelt3W gdelt3W) throws Exception {
        return gdelt3W;
    }
}
