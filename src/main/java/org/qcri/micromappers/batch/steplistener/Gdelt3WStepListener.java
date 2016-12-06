package org.qcri.micromappers.batch.steplistener;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

/**
 * Created by jlucas on 12/4/16.
 */
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Gdelt3WStepListener implements StepExecutionListener, ApplicationContextAware {
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
        MultiResourceItemReader reader = (MultiResourceItemReader) applicationContext.getBean("multiResourceItemReader3W");
        try {
            resources = applicationContext.getResources(filePattern);
            reader.setResources(resources);
        } catch (IOException ex) {
            System.out.println( "Unable to set file resources to bean multiResourceItemReader: " +  ex);
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)
                && stepExecution.getStatus().equals(BatchStatus.COMPLETED)
                && resources.length > 0) {

            for (Resource resource : resources) {
                try {
                    File oldFile = new File(resource.getFile().getAbsolutePath());
                    File newFile = new File(resource.getFile().getAbsolutePath() + ".processed");
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
