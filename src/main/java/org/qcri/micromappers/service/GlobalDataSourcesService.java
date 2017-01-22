package org.qcri.micromappers.service;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.qcri.micromappers.entity.*;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.models.CollectionDetailsInfo;
import org.qcri.micromappers.models.GlobalDataSources;
import org.qcri.micromappers.models.WordCloud;
import org.qcri.micromappers.repository.GlideMasterRepository;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.GlobalDataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jlucas on 1/16/17.
 */
@Service
public class GlobalDataSourcesService {
    private static Logger logger = Logger.getLogger(GlobalDataSourcesService.class);

    @Autowired
    GlobalEventDefinitionService globalEventDefinitionService;

    @Autowired
    GlideMasterService glideMasterService;

    @Autowired
    CollectionLogService collectionLogService;

    public List<GlobalDataSources> findAll()
    {

        List<GlobalEventDefinition> globalEventDefinitionList = globalEventDefinitionService.findAllByState(Constants.SNOPES_STATE_ACTIVE);
        List<GlideMaster> glideMasterList = glideMasterService.findAll();

        List<GlobalDataSources> dataSources = new ArrayList<GlobalDataSources>();
        dataSources = this.populateGlideMaster(glideMasterList, dataSources);
        dataSources = this.populateSnopes(globalEventDefinitionList, dataSources);

        //sorting
        Comparator<GlobalDataSources> globalDataSourcesComparator = (o1, o2)->o1.getCreatedAt().compareTo(o2.getCreatedAt());
        dataSources.sort(globalDataSourcesComparator.reversed());

        return dataSources;
    }

    public List<GlobalDataSources> findBySearch(String searchWord)
    {

        List<GlobalEventDefinition> globalEventDefinitionList = globalEventDefinitionService.findAllByStateAndTags(Constants.SNOPES_STATE_ACTIVE, searchWord);

        List<GlobalDataSources> dataSources = new ArrayList<GlobalDataSources>();
        dataSources = this.populateSnopes(globalEventDefinitionList, dataSources);

        Comparator<GlobalDataSources> globalDataSourcesComparator = (o1, o2)->o1.getCreatedAt().compareTo(o2.getCreatedAt());
        dataSources.sort(globalDataSourcesComparator.reversed());

        return dataSources;
    }

    private List<GlobalDataSources> populateSnopes(List<GlobalEventDefinition> globalEventDefinitionList, List<GlobalDataSources> dataSources){
        globalEventDefinitionList.forEach((temp) -> {
            try {
                GlobalDataSources a = new GlobalDataSources();
                a.setSource(GlobalDataSourceType.SNOPES.getValue());
                a.setGlobalEventDefinition(temp);

                List<CollectionDetailsInfo> collectionDetailsInfoList = this.getCollectionDetails(temp.getCollection());
                a.setCollectionDetailsInfoList(collectionDetailsInfoList);
                a.setSocialCollectionTotal(calculateSocialMediaDatasetCollectionTotal(collectionDetailsInfoList));

                a.setKeywords(this.getKeyWords(collectionDetailsInfoList, temp));
                a.setCreatedAt(temp.getCreatedAt());
                dataSources.add(a);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return dataSources;
    }

    private List<GlobalDataSources> populateGlideMaster(List<GlideMaster> glideMasterList, List<GlobalDataSources> dataSources){
        glideMasterList.forEach((temp) -> {
            try {
                GlobalDataSources a = new GlobalDataSources();
                a.setSource(GlobalDataSourceType.GDELT.getValue());
                a.setGlideMaster(temp);
                a.setGdelt3WList(temp.getGdelt3WList());
                a.setGdeltMMICList(temp.getGdeltMMICList());

                List<CollectionDetailsInfo> collectionDetailsInfoList = this.getCollectionDetails(temp.getCollection());
                a.setCollectionDetailsInfoList(collectionDetailsInfoList);
                a.setSocialCollectionTotal(calculateSocialMediaDatasetCollectionTotal(collectionDetailsInfoList));

                a.setGdeltCollectionTotal(temp.getGdelt3WList().size() + temp.getGdeltMMICList().size());
                a.setGdelt3WArticleTotal(temp.getTotalCount3WArticle());
                a.setGdelt3WImageTotal(temp.getTotalCount3WImage());
                a.setGdeltMMICArticleTotal(temp.getTotalCountMMICArticle());
                a.setGdeltMMICImageTotal(temp.getTotalCountMMICImage());
                a.setKeywords(this.getKeyWords(collectionDetailsInfoList, null));
                a.setCreatedAt(temp.getCreatedAt());
                dataSources.add(a);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return dataSources;
    }

    private long calculateSocialMediaDatasetCollectionTotal(List<CollectionDetailsInfo> collectionDetailsInfoList){
        long totalCount =0;

        for (CollectionDetailsInfo temp : collectionDetailsInfoList) {
            if(temp.getCount() != null){
                totalCount = totalCount + temp.getCount();
            }

        }

        return totalCount;
    }

    private List<WordCloud> getKeyWords(List<CollectionDetailsInfo> collectionDetailsInfo, GlobalEventDefinition globalEventDefinition){

        List<WordCloud> keywords = new ArrayList<WordCloud>();

        collectionDetailsInfo.forEach((temp) -> {
            try {
                if(temp.getTrack() != null && !temp.getTrack().isEmpty()){
                    String[] parts = temp.getTrack().split(",");
                    keywords.addAll(this.createWordCloud(parts));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if(keywords.isEmpty() && globalEventDefinition!=null){
            if(globalEventDefinition.getArticleTag() != null){
                String[] parts = globalEventDefinition.getArticleTag().split(",");
                keywords.addAll(this.createWordCloud(parts));
            }
            else{
                String[] parts = globalEventDefinition.getSearchKeyword().split(",");
                keywords.addAll(this.createWordCloud(parts));
            }
        }

        return keywords;

    }

    public List<WordCloud> sycronizeKeyWord(List<GlobalDataSources> globalDataSourcesList){

        List<WordCloud> wordCloudList = new ArrayList<WordCloud>();

        globalDataSourcesList.forEach((temp) -> {
            wordCloudList.addAll(temp.getKeywords());
        });

        Map<String, Long> result =
                wordCloudList.stream().collect(
                        Collectors.groupingBy(
                                WordCloud::getText, Collectors.counting()
                        )
                );

        List<WordCloud> finalWordCloudList = new ArrayList<WordCloud>();

        result.forEach((k,v)->{
            System.out.println("Item : " + k + " Count : " + v);
            finalWordCloudList.add(new WordCloud(k,v*10));
        });

        return finalWordCloudList;
    }

    public JSONArray KeywordToJsonArray(List<WordCloud> wordClouds){

        JSONArray jsonArray = new JSONArray();

        wordClouds.forEach((temp) -> {
            jsonArray.add(temp.toJson());
        });

        return jsonArray;
    }

    private List<WordCloud> createWordCloud(String[] words){
        List<WordCloud> keywords = new ArrayList<WordCloud>();

        if(Constants.STOP_WORDS == null){
            try {
                Constants.populateStopWords();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i < words.length; i++){
            if(!Constants.STOP_WORDS.contains(words[i]) && !words[i].isEmpty())
            {
                keywords.add(new WordCloud(words[i], 0));
            }
        }

        return keywords;

    }

    private List<CollectionDetailsInfo> getCollectionDetails(List<Collection> collectionList){
        List<CollectionDetailsInfo> collectionDetailsInfoList = new ArrayList<CollectionDetailsInfo>();

        collectionList.forEach((temp) -> {
            try {
                long collectionCount = collectionLogService.getCountByCollectionId(temp.getId());
                CollectionDetailsInfo collectionDetailsInfo = temp.toCollectionDetailsInfo();
                collectionDetailsInfo.setCount(collectionCount);
                collectionDetailsInfoList.add(collectionDetailsInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return collectionDetailsInfoList;
    }

    private int calcualteFacebookOnlyCollectionCount(List<Collection> collectionList){
        int count = 0;

        List<Collection> result = collectionList.stream() 			//convert list to stream
                .filter(line -> !CollectionType.FACEBOOK.getValue().equals(line))	//filters the line, equals to "mkyong"
                .collect(Collectors.toList());


        return result.size();
    }

    private int calcualteTwitterOnlyCollectionCount(List<Collection> collectionList){
        int count = 0;

        List<Collection> result = collectionList.stream() 			//convert list to stream
                .filter(line -> !CollectionType.FACEBOOK.getValue().equals(line))	//filters the line, equals to "mkyong"
                .collect(Collectors.toList());


        return result.size();
    }

    private int calcualteAllCollectionCount(List<Collection> collectionList){
        int count = 0;

        List<Collection> result = collectionList.stream() 			//convert list to stream
                .filter(line -> !CollectionType.ALL.getValue().equals(line))	//filters the line, equals to "mkyong"
                .collect(Collectors.toList());


        return result.size();
    }


}
