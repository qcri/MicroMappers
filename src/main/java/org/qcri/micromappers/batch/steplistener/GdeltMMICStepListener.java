package org.qcri.micromappers.batch.steplistener;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.qcri.micromappers.utility.Constants;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

/**
 * Created by jlucas on 12/1/16.
 */
public class GdeltMMICStepListener implements StepExecutionListener, ApplicationContextAware {
    private Resource[] resources;
    private ApplicationContext applicationContext;
    private String filePattern;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }
    @Override
    public void beforeStep(StepExecution stepExecution) {
        MultiResourceItemReader reader = (MultiResourceItemReader) applicationContext.getBean("multiResourceItemReader");
        try {
            resources = applicationContext.getResources(filePattern);
            reader.setResources(resources);
        } catch (IOException ex) {
            System.out.println( "Unable to set file resources to bean multiResourceItemReader: " +  ex);
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        /*if (stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)
                && stepExecution.getStatus().equals(BatchStatus.COMPLETED)
                && resources.length > 0) { */
        if (!stepExecution.getExitStatus().equals(ExitStatus.EXECUTING)
                && !stepExecution.getStatus().equals(ExitStatus.UNKNOWN)
                && resources.length > 0) {

            for (Resource resource : resources) {
                try {
                    File oldFile = new File(resource.getFile().getAbsolutePath());
                    File newFile = new File(resource.getFile().getAbsolutePath() + Constants.GDELT_PROCESSED_EXTENTION);
                    FileUtils.copyFile(oldFile, newFile);
                    oldFile.delete();
                } catch (IOException ex) {
                    System.out.println("Encountered problem when trying to remove the processed file(s): " +  ex);
                }
            }
        }

        return stepExecution.getExitStatus();
    }
}
