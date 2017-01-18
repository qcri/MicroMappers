package org.qcri.micromappers.service;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.qcri.micromappers.entity.*;
import org.qcri.micromappers.models.CollectionDetailsInfo;
import org.qcri.micromappers.models.GlobalDataSources;
import org.qcri.micromappers.repository.GlideMasterRepository;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.GlobalDataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

        //dataSources.sort((GlobalDataSources o1, GlobalDataSources o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));

        Comparator<GlobalDataSources> globalDataSourcesComparator = (o1, o2)->o1.getCreatedAt().compareTo(o2.getCreatedAt());
        dataSources.sort(globalDataSourcesComparator.reversed());

        return dataSources;
    }

    private List<GlobalDataSources> populateSnopes(List<GlobalEventDefinition> globalEventDefinitionList, List<GlobalDataSources> dataSources){
        globalEventDefinitionList.forEach((temp) -> {
            try {
                GlobalDataSources a = new GlobalDataSources();
                a.setSource(GlobalDataSourceType.SNOPES.getValue());
                a.setIsGdideMasterDataSet(false);
                a.setIsSnopesDataSet(true);
                a.setGlobalEventDefinition(temp);

                List<CollectionDetailsInfo> collectionDetailsInfoList = this.getCollectionDetails(temp.getCollection());
                a.setCollectionDetailsInfoList(collectionDetailsInfoList);
                a.setSocialCollectionTotal(calculateSocialMediaDatasetCollectionTotal(collectionDetailsInfoList));

                a.setKeywords(this.getKeyWords(collectionDetailsInfoList));
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
                a.setIsGdideMasterDataSet(true);
                a.setIsSnopesDataSet(false);
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
                a.setKeywords(this.getKeyWords(collectionDetailsInfoList));
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

    private ArrayList<String> getKeyWords(List<CollectionDetailsInfo> collectionDetailsInfo){

        ArrayList<String> keywords = new ArrayList<String>();

        collectionDetailsInfo.forEach((temp) -> {
            try {
                if(temp.getTrack() != null && !temp.getTrack().isEmpty()){
                    keywords.add(temp.getTrack());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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
