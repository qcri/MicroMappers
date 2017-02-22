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
            logger.info("need to wait more for Snopes");
            return RepeatStatus.FINISHED;
        }
        else{
            Util.timeOfLastSnopesProcessingMillis = System.currentTimeMillis();
        }

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setThrowExceptionOnScriptError(false);

        try {
            String baseUrl = configProperties.getProperty(MicromappersConfigurationProperty.SNOPES_COM_BASE_URL);
            String searchUrl = configProperties.getProperty(MicromappersConfigurationProperty.SNOPES_COM_NEWS_URL);
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlElement> items = (List<HtmlElement>) page.getByXPath(".//div[@id='main-list']/article") ;
            if(items.isEmpty()){
                logger.info("No items found !");
            }else{
                for(HtmlElement item : items){
                    GlobalEventDefinition globalEventDefinition = new GlobalEventDefinition();

                    HtmlMeta meta = ((HtmlMeta)item.getFirstByXPath("meta[@itemprop='mainEntityOfPage']"));

                    if(meta == null){
                        meta = ((HtmlMeta)item.getFirstByXPath("meta[@itemprop='url']"));
                    }

                    String itemUrl = meta.getContentAttribute() ;


                    HtmlElement itemTtitle = (HtmlElement)item.getFirstByXPath("a/h2[@class='article-link-title']");
                    String title = itemTtitle.asText();

                    if(globalEventDefinitionService.findByEventUrl(itemUrl) == null){

                        String tempKeywords = itemUrl;
                        if(tempKeywords.endsWith("/")){
                            tempKeywords = tempKeywords.substring(0,tempKeywords.length() - 1);
                            int lastStart = tempKeywords.lastIndexOf("/");
                            tempKeywords = tempKeywords.substring(lastStart + 1, tempKeywords.length());
                        }
                        globalEventDefinition.setSearchKeyword(tempKeywords.replaceAll("/", "").replaceAll("-", ","));
                        globalEventDefinition.setTitle(title);
                        globalEventDefinition.setEventUrl(itemUrl);

                        HtmlMeta metaDatePublished = ((HtmlMeta)item.getFirstByXPath("meta[@itemprop='datePublished']"));
                        globalEventDefinition.setDatePublished(metaDatePublished.getContentAttribute());

                        globalEventDefinition.setAuthor("snopes");

                        globalEventDefinition = this.getEachFact(client, globalEventDefinition);
                        if(globalEventDefinition.getArticleTag() == null || globalEventDefinition.getArticleTag().isEmpty() ){
                            globalEventDefinition.setArticleTag(globalEventDefinition.getSearchKeyword());
                        }
                        globalEventDefinition.setState(Constants.SNOPES_STATE_ACTIVE);
                        globalEventDefinitionService.create(globalEventDefinition);
                    }
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
            List<HtmlElement> items = (List<HtmlElement>) page.getByXPath(".//div[@class='body-content']/article") ;
            if(items.isEmpty()) {
                logger.info("No items found !");
            }else{
                for(HtmlElement item : items){

                    HtmlElement itemDescription = ((HtmlElement)item.getFirstByXPath(".//header/h2[@itemprop='description']"));

                    String description = itemDescription.asText();
                    globalEventDefinition.setDescription(description);


                    HtmlElement span1 = ((HtmlElement)item.getFirstByXPath(".//p[@itemprop='claimReviewed']"));
                    if(span1!=null){
                        String clainReviewed = span1.asText();
                        globalEventDefinition.setClaimReviewed(clainReviewed);
                    }


                    HtmlSpan span2 = ((HtmlSpan)item.getFirstByXPath(".//span[@itemprop='reviewRating']/span[@itemprop='alternateName']"));

                    if(span2 != null){
                        String reviewRating = span2.asText();
                        globalEventDefinition.setClainReviewRating(reviewRating);

                    }
                    //item.getByXPath(".//div[@class='entry-content article-text legacy']/footer[@class='article-footer']/div[@class='article-info-box']/p[@class='tag-box']/a")
                    List<HtmlAnchor> tags = (List<HtmlAnchor>)item.getByXPath(".//.//div[@class='entry-content article-text legacy']/footer[@class='article-footer']/div[@class='article-info-box']/p[@class='tag-box']/a");
                   // List<HtmlAnchor> tags =  (List<HtmlAnchor>)item.getByXPath(".//div[@class='article-tags clearfix']/ul/li/a");
                    if(tags != null){
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
        }
        catch (Exception e){
            logger.error("getEachFact: " + e);
        }

        return globalEventDefinition;
    }

}
