package org.qcri.micromappers.batch.tasklet;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.service.GlobalEventDefinitionService;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.Util;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jlucas on 2/21/17.
 */
public class GoogleNewsFetch implements Tasklet {

    private static Logger logger = Logger.getLogger(GoogleNewsFetch.class);
    private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();

    @Autowired
    GlobalEventDefinitionService globalEventDefinitionService;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        if(!Util.isTimeToGoogleNewsFetchRun()){
            logger.info("need to wait more for Google News");
            return RepeatStatus.FINISHED;
        }
        else{
            logger.info("running for Google News");
            Util.timeOfLastGoogleNewsProcessingMillis = System.currentTimeMillis();
        }

        URL url = new URL(configProperties.getProperty(MicromappersConfigurationProperty.GOOGLE_NEWS_RSS_URL));
        HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
        // Reading the feed
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpcon));
        List entries = feed.getEntries();
        String author = "google news";
        entries.forEach(item -> {
          //  System.out.println(item);
            String articleLink = ((SyndEntryImpl) item).getLink();
            String title = ((SyndEntryImpl) item).getTitle();
            //findByEventURLAndTitle
            GlobalEventDefinition definition = globalEventDefinitionService.findByEventURLAndTitleAndAuthor(articleLink, title, author);

            if(definition == null){

                String publishedDate = ((SyndEntryImpl) item).getPublishedDate().toString();

                GlobalEventDefinition globalEventDefinition =
                        new GlobalEventDefinition(title, publishedDate,articleLink, author);
                globalEventDefinition.setDescription(title);
                globalEventDefinition.setState(Constants.SNOPES_STATE_ACTIVE);

                globalEventDefinitionService.create(globalEventDefinition);
            }

        });

        return RepeatStatus.FINISHED;
    }
}
