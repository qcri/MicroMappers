package org.qcri.micromappers.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.SentimentAnalysis;
import org.qcri.micromappers.repository.SentimentAnalysisRepository;
import org.qcri.micromappers.utility.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by jlucas on 2/19/17.
 */
@Service
public class SentimentAnalysisService {
    private static Logger logger = Logger.getLogger(SentimentAnalysisService.class);

    @Inject
    SentimentAnalysisRepository sentimentAnalysisRepository;

    public Page<SentimentAnalysis> findByStateAndCollectionId(Integer pageNumber, String state, long collection_id){

        PageRequest request =
                new PageRequest(pageNumber - 1, Constants.DEFAULT_PAGE_SIZE, Sort.Direction.DESC, "createdAt");

        return sentimentAnalysisRepository.findByCollectionIdAndState(request, collection_id, state);
    }

    public List<SentimentAnalysis> findByStateAndCollectionId(String state, long collection_id){

        return sentimentAnalysisRepository.findByCollectionIdAndState(collection_id, state);
    }

}
