package org.qcri.micromappers.batch.tasklet;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.log4j.Logger;
import org.qcri.micromappers.batch.processor.GdeltMMICMediaProcessor;
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

import java.util.List;
import java.util.logging.Level;

/**
 * Created by jlucas on 12/8/16.
 */
public class SnopesNewsFetch implements Tasklet {

    private static Logger logger = Logger.getLogger(SnopesNewsFetch.class);
    private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();
    @Autowired
    GlobalEventDefinitionService globalEventDefinitionService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        if(!Util.isTimeToSnopesFetchRun()){
            logger.info("need to wait more");
            return RepeatStatus.FINISHED;
        }

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            String baseUrl = configProperties.getProperty(MicromappersConfigurationProperty.SNOPES_COM_BASE_URL);
            String searchUrl = configProperties.getProperty(MicromappersConfigurationProperty.SNOPES_COM_NEWS_URL);
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlElement> items = (List<HtmlElement>) page.getByXPath(".//ul[@class='post-list']/li") ;
            if(items.isEmpty()){
                logger.info("No items found !");
            }else{
                int index = 0;
                for(HtmlElement item : items){
                    GlobalEventDefinition globalEventDefinition = new GlobalEventDefinition();

                    HtmlAnchor itemAnchor1 = ((HtmlAnchor)item.getByXPath("//div[@class='right-side']/h4[@class='title']/a").get(index));

                    String itemUrl = itemAnchor1.getHrefAttribute() ;

                    if(globalEventDefinitionService.findByEventUrl(baseUrl+itemUrl) == null){
                        String title = itemAnchor1.asText();

                        globalEventDefinition.setSearchKeyword(itemUrl.replaceAll("/", "").replaceAll("-", ","));
                        globalEventDefinition.setTitle(title);
                        globalEventDefinition.setEventUrl(baseUrl+itemUrl);

                        HtmlElement itemAnchor2 = ((HtmlElement)item.getByXPath("//div[@class='right-side']/p[@class='body']/span[@class='label']").get(index));
                        String description = itemAnchor2.asText();
                        globalEventDefinition.setDescription(description);


                        HtmlElement itemAnchor3 = ((HtmlElement) item.getByXPath("//div[@class='right-side']/div[@class='meta']/a[@class='author']/span[@itemprop='author']").get(index));
                        String author = itemAnchor3.asText();
                        globalEventDefinition.setAuthor(author);

                        globalEventDefinition = this.getEachFact(client, globalEventDefinition);
                        globalEventDefinition.setState(Constants.SNOPES_STATE_ACTIVE);
                        globalEventDefinitionService.create(globalEventDefinition);
                    }

                    index++;
                }
            }
        }catch(Exception e){
            logger.error("RepeatStatus: " + e);
        }
        return RepeatStatus.FINISHED;
    }

    public GlobalEventDefinition getEachFact(WebClient client, GlobalEventDefinition globalEventDefinition){
        try{
            HtmlPage page = client.getPage(globalEventDefinition.getEventUrl());
            List<HtmlElement> items = (List<HtmlElement>) page.getByXPath(".//div[@class='wordpress']/div[@class='content-wrapper']/article") ;
            if(items.isEmpty()) {
                logger.info("No items found !");
            }else{
                for(HtmlElement item : items){
                    HtmlMeta meta = ((HtmlMeta)item.getFirstByXPath(".//meta[@itemprop='datePublished']"));
                    String datePublished = meta.getContentAttribute();
                    globalEventDefinition.setDatePublished(datePublished);

                    HtmlSpan span1 = item.getFirstByXPath(".//span[@itemprop='claimReviewed']");
                    if(span1!=null){
                        String clainReviewed = span1.asText();
                        globalEventDefinition.setClaimReviewed(clainReviewed);
                    }


                    HtmlSpan span2 = item.getFirstByXPath(".//span[@itemprop='reviewRating']/span[@itemprop='alternateName']");

                    if(span2 != null){
                        String reviewRating = span2.asText();
                        globalEventDefinition.setClainReviewRating(reviewRating);

                    }

                    List<HtmlAnchor> tags =  (List<HtmlAnchor>)item.getByXPath(".//div[@class='article-tags clearfix']/ul/li/a");

                    StringBuilder tagInfo = new StringBuilder();
                    for(HtmlAnchor tag : tags){
                        if(tagInfo.length() > 0){
                            tagInfo.append(",");
                        }
                        tagInfo.append(tag.asText());

                    }
                    globalEventDefinition.setArticleTag(tagInfo.toString());
                }
            }
        }
        catch (Exception e){
            logger.error("getEachFact: " + e);
        }

        return globalEventDefinition;
    }

}
