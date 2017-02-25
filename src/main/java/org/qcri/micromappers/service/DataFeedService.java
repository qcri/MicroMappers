package org.qcri.micromappers.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.inject.Inject;
import javax.json.JsonObject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.DataFeed;
import org.qcri.micromappers.entity.TextDisambiguityAnalysis;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.DataFeedRepository;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.Status;
import org.qcri.micromappers.utility.Util;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class DataFeedService
{
	@Inject
	private DataFeedRepository dataFeedRepository;
	@Inject
	private TextDisambiguityService textDisambiguityService;
	@Inject
	private Util util;

	private static Logger logger = Logger.getLogger(DataFeedService.class);
	private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();


	private DataFeed create(DataFeed dataFeed)
	{
		dataFeed.setComputerVisionEnabled(dataFeed.getCollection().getComputerVisionEnabled());

		try{
			return dataFeedRepository.save(dataFeed);
		}catch(DataIntegrityViolationException de){
			logger.warn("DataIntegrityViolationException: Cannot save data_feed. As this record already exists for this colleciton.");
			throw new DataIntegrityViolationException("DataIntegrityViolationException: Cannot save data_feed. As this record already exists for this colleciton.");
		}
		catch (Exception e) {
			logger.error("Error while persisting to dataFeed", e);
			throw new MicromappersServiceException("Exception while persisting to dataFeed", e);
		}
	}

	/**
	 * Returning DataFeed where parentFeed is null by provider and feedId
	 * @param provider
	 * @param feedId
	 * @return
	 */
	public DataFeed findByProviderAndFeedId(CollectionType provider, String feedId)
	{
		try{
			return dataFeedRepository.findByProviderAndFeedIdAndParentFeedIsNull(provider, feedId);
		}catch (Exception e) {
			logger.error("Error while fetching dataFeed by provider & feedId", e);
			throw new MicromappersServiceException("Error while fetching dataFeed by provider & feedId", e);
		}
	}

	public List<DataFeed> findByComputerVisionEnabled(boolean computerVisionEnabled){
		return dataFeedRepository.findByComputerVisionEnabled(computerVisionEnabled);
	}

	public DataFeed persistToDbAndFile(DataFeed dataFeed, JsonObject feed, Boolean toDisambiguateText)
	{
		try{
			DataFeed parentFeed = findByProviderAndFeedId(dataFeed.getProvider(), dataFeed.getFeedId());
			if(parentFeed != null){
				dataFeed.setParentFeed(parentFeed);
				if(parentFeed.getCollection().getId() == dataFeed.getCollection().getId()){
					return null;
				}
			}
			try{
				create(dataFeed);
				persistFeedToFile(dataFeed, feed.toString());
				if(toDisambiguateText){
					persistToTextDisambiguitor(dataFeed, feed);
				}
				return dataFeed;
			}catch(DataIntegrityViolationException de){
				logger.warn("DataIntegrityViolationException: Cannot save data_feed. As this record already exists for this colleciton.");
				return null;
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				return null;
			}
		}catch (MicromappersServiceException e) {
			logger.error(e.getMessage(), e);
			throw new MicromappersServiceException(e.getMessage(), e);
		}
	}

	private void persistFeedToFile(DataFeed dataFeed, String feed) {
		Path parentPath = Paths.get(configProperties.getProperty(MicromappersConfigurationProperty.Feed_PATH), 
				dataFeed.getCollection().getCode(), dataFeed.getProvider().toString());

		if(util.createDirectories(parentPath)){
			Path filePath = Paths.get(parentPath.toString(), dataFeed.getFeedId());
			util.writeToFile(filePath, feed);
		}
	}
	
	
	public void persistToTextDisambiguitor(DataFeed dataFeed, JsonObject feed){
		try{
			if(dataFeed.getProvider() == CollectionType.TWITTER){
				TextDisambiguityAnalysis textDisambiguityAnalysis = new TextDisambiguityAnalysis();
				textDisambiguityAnalysis.setCollectionId(dataFeed.getCollection().getId());
				textDisambiguityAnalysis.setFeedId(dataFeed.getFeedId());
				textDisambiguityAnalysis.setFeedText(feed.getString("text"));
				textDisambiguityAnalysis.setStatus(Status.ONGOING);

				textDisambiguityService.saveOrUpdate(textDisambiguityAnalysis);
			}
		}catch (Exception e) {
			logger.error("Exception while persisting to textDisambiguityAnalysis",e);
		}
	}
}
