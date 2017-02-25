package org.qcri.micromappers.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.TextDisambiguityAnalysis;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.TextDisambiguityRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * @author Kushal
 *
 */
@Service
public class TextDisambiguityService {
	private static Logger logger = Logger.getLogger(TextDisambiguityService.class);

	@Inject
	TextDisambiguityRepository textDisambiguityRepository;

	public TextDisambiguityAnalysis saveOrUpdate(TextDisambiguityAnalysis textDisambiguityAnalysis)
	{
		try{
			return textDisambiguityRepository.save(textDisambiguityAnalysis);
		}catch(DataIntegrityViolationException de){
			logger.warn("DataIntegrityViolationException: Cannot save textDisambiguityAnalysis. As this record already exists for this collection.");
			throw new DataIntegrityViolationException("DataIntegrityViolationException: Cannot save textDisambiguityAnalysis. As this record already exists for this collection.");
		}
		catch (Exception e) {
			logger.error("Error while persisting to textDisambiguityAnalysis", e);
			throw new MicromappersServiceException("Exception while persisting to textDisambiguityAnalysis", e);
		}
	}
}
