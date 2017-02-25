package org.qcri.micromappers.controller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.qcri.micromappers.entity.Gdelt3W;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.entity.SentimentAnalysis;
import org.qcri.micromappers.models.GlobalDataSources;
import org.qcri.micromappers.models.PageInfo;
import org.qcri.micromappers.models.WordCloud;
import org.qcri.micromappers.service.GlobalDataSourcesService;
import org.qcri.micromappers.service.SentimentAnalysisService;
import org.qcri.micromappers.utility.ComputerVisionStatus;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.TextAnalyticsStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jlucas on 1/17/17.
 */
@Controller
@RequestMapping("dashboard")
public class DashboardController {
    private static Logger logger = Logger.getLogger(DashboardController.class);

    @Autowired
    GlobalDataSourcesService globalDataSourcesService;

    @Autowired
    SentimentAnalysisService sentimentAnalysisService;

    @RequestMapping(value={"/global"})
    public String globalOverview(Model model, HttpServletRequest request,
                                 @RequestParam(value = "page", defaultValue = "1") String page,
                                 @RequestParam(value = "q", defaultValue = "") String searchWord) {

        int pageNumber = Integer.valueOf(page);
       // searchWord = "trump";
        List<GlobalDataSources> globalDataSourcesList = globalDataSourcesService.findAll(searchWord);

        PageRequest pageRequestreq =
                new PageRequest(pageNumber - 1, Constants.DEFAULT_PAGE_SIZE, Sort.Direction.DESC, "createdAt");

        int max = (Constants.DEFAULT_PAGE_SIZE*(pageNumber+1)>globalDataSourcesList.size())?
                globalDataSourcesList.size(): Constants.DEFAULT_PAGE_SIZE*(pageNumber+1);

        int i = (pageNumber -1) * Constants.DEFAULT_PAGE_SIZE;
        int j = i + (Constants.DEFAULT_PAGE_SIZE-1);

        if(j >= globalDataSourcesList.size()){
            j = globalDataSourcesList.size() - 1;
        }

        //logger.debug("sublist : " + i + " - " + j);

        List<GlobalDataSources> pageDataSet = new ArrayList<GlobalDataSources>();
        for(int k= 0; k < globalDataSourcesList.size(); k++){
            if(k>=i && k <= j){
                pageDataSet.add(globalDataSourcesList.get(k));
            }
        }

        Page<GlobalDataSources> pages =
                new PageImpl<GlobalDataSources>(pageDataSet, pageRequestreq, globalDataSourcesList.size());

        PageInfo<GlobalDataSources> pageInfo = new PageInfo<>(pages);
        pageInfo.setList(pages.getContent());

        model.addAttribute("page", pageInfo);
        model.addAttribute("globaldataset", globalDataSourcesList);
        model.addAttribute("wordClouds", getWordCloudModelData(globalDataSourcesList));

        return "/dashboard/global";
    }

    @RequestMapping(value={"/keywordSentiment"})
    public String getMMICData(Model model, HttpServletRequest request,HttpServletResponse response,
                              @RequestParam(value = "cid", defaultValue = "0") String cid,
                              @RequestParam(value = "dw", defaultValue = "") String dw) {


        long collection_Id = Long.valueOf(cid);

        if(dw != null){
            if(!dw.isEmpty())
            {
                this.downloadSentimentAnalysisData(response, collection_Id);
            }
        }

        model = this.sentimentBubbleScore(collection_Id, model);

        model.addAttribute("page", sentimentAnalysisService.findByStateAndCollectionId(TextAnalyticsStatus.COMPLETED, collection_Id));
        model.addAttribute("cid",collection_Id);

        return "/dashboard/keywordSentiment";
    }


    private String getWordCloudModelData(List<GlobalDataSources> globalDataSourcesList){

        List<WordCloud> wordClouds = globalDataSourcesService.sycronizeKeyWord(globalDataSourcesList);

        JSONArray jsonArray = globalDataSourcesService.KeywordToJsonArray(wordClouds);

        return jsonArray.toJSONString();
    }

    private Model sentimentBubbleScore(long collection_Id, Model model){

        List<SentimentAnalysis> sentimentAnalysisList =
                sentimentAnalysisService.findByStateAndCollectionId(TextAnalyticsStatus.COMPLETED, collection_Id);

        try{

            long negative_count = sentimentAnalysisList
                        .stream()
                        .filter(u -> u.getNegative().doubleValue() >= 0.5)
                        .collect(Collectors.counting());

            long positive_count = sentimentAnalysisList
                    .stream()
                    .filter(u -> u.getPositive().doubleValue() > 0.5)
                    .collect(Collectors.counting());

            double neg_percent = ((double)negative_count / (double)sentimentAnalysisList.size()) *100;
            double pos_percent = ((double)positive_count / (double)sentimentAnalysisList.size()) *100;

            model.addAttribute("neg_percent", Math.round(neg_percent));
            model.addAttribute("pos_percent", Math.round(pos_percent));

            return model;
        }
        catch (Exception e){
            logger.error("sentimentBubbleScore: " + e);
            return model;
        }

    }

    private void downloadSentimentAnalysisData(HttpServletResponse response, long collection_Id){
        try{
            List<SentimentAnalysis> sentimentAnalysisList =
                    sentimentAnalysisService.findByStateAndCollectionId(TextAnalyticsStatus.COMPLETED, collection_Id);

            response.setContentType("text/csv");
            String reportName = "sentiment_" + collection_Id+"_"+new Date().getTime()+".csv";
            response.setHeader("Content-disposition", "attachment;filename="+reportName);
            String header = "text,positive,negative\r\n";

            response.getOutputStream().print(header);

            sentimentAnalysisList.forEach((temp) -> {
                try {
                    response.getOutputStream().print(this.buildSentimentAnalysisDataOutPutString(temp));

                } catch (IOException e) {
                    logger.error("downloadSentimentAnalysisData IOException1: " + e);
                }
            });

            try {
                response.getOutputStream().flush();
            } catch (IOException e) {
                logger.error("downloadSentimentAnalysisData IOException2: " + e);
            }
        }
        catch(Exception e){
            logger.error("downloadSentimentAnalysisData: " + e);
        }
    }

    private String buildSentimentAnalysisDataOutPutString(SentimentAnalysis temp){
        String out = StringEscapeUtils.escapeCsv(temp.getFeedText()) + ","
                    + StringEscapeUtils.escapeCsv(temp.getPositive().toString()) + ","
                    + StringEscapeUtils.escapeCsv(temp.getNegative().toString()) + "\r\n";
        return out;
    }
}
