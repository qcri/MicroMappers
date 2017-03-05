package org.qcri.micromappers.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.SentimentAnalysis;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.SentimentAnalysisRepository;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.TextAnalyticsStatus;
import org.springframework.dao.DataIntegrityViolationException;
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

    public Page<SentimentAnalysis> findByStateAndCollectionId(Integer pageNumber, TextAnalyticsStatus state, Long collectionId){

        PageRequest request =
                new PageRequest(pageNumber - 1, Constants.DEFAULT_PAGE_SIZE, Sort.Direction.DESC, "createdAt");

        return sentimentAnalysisRepository.findByCollectionIdAndState(collectionId, state, request);
    }

    public List<SentimentAnalysis> findByStateAndCollectionId(TextAnalyticsStatus state, Long collectionId){

        return sentimentAnalysisRepository.findByCollectionIdAndState(collectionId, state);
    }
    
    
    public SentimentAnalysis saveOrUpdate(SentimentAnalysis sentimentAnalysis)
	{
		try{
			return sentimentAnalysisRepository.save(sentimentAnalysis);
		}catch(DataIntegrityViolationException de){
			logger.warn("DataIntegrityViolationException: Cannot save sentimentAnalysis. As this record already exists for this collection.", de);
			throw new DataIntegrityViolationException("DataIntegrityViolationException: Cannot save sentimentAnalysis. As this record already exists for this collection.", de);
		}
		catch (Exception e) {
			logger.error("Error while persisting to sentimentAnalysis", e);
			throw new MicromappersServiceException("Exception while persisting to sentimentAnalysis", e);
		}
	}

}
