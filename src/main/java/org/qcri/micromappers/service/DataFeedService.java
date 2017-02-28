package org.qcri.micromappers.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.json.JsonObject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.DataFeed;
import org.qcri.micromappers.entity.SentimentAnalysis;
import org.qcri.micromappers.entity.TextDisambiguityAnalysis;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.DataFeedRepository;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.TextAnalyticsStatus;
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
	private SentimentAnalysisService sentimentAnalysisService;
	@Inject
	private Util util;

	private static Logger logger = Logger.getLogger(DataFeedService.class);
	private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);

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
				persistToSentimentAnalysis(dataFeed, feed);
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
				textDisambiguityAnalysis.setCreatedAt(formatTwitterDate(feed.getString("created_at")));
				textDisambiguityAnalysis.setStatus(TextAnalyticsStatus.ONGOING);

				textDisambiguityService.saveOrUpdate(textDisambiguityAnalysis);
			}
		}catch (Exception e) {
			logger.error("Exception while persisting to textDisambiguityAnalysis",e);
		}
	}
	
	public void persistToSentimentAnalysis(DataFeed dataFeed, JsonObject feed){
		try{
			if(dataFeed.getProvider() == CollectionType.TWITTER){
				SentimentAnalysis sentimentAnalysis = new SentimentAnalysis();
				sentimentAnalysis.setCollectionId(dataFeed.getCollection().getId());
				sentimentAnalysis.setFeedId(dataFeed.getFeedId());
				sentimentAnalysis.setFeedText(feed.getString("text"));
				sentimentAnalysis.setCreatedAt(formatTwitterDate(feed.getString("created_at")));
				sentimentAnalysis.setState(TextAnalyticsStatus.ONGOING);
				
				sentimentAnalysisService.saveOrUpdate(sentimentAnalysis);
			}
		}catch (Exception e) {
			logger.error("Exception while persisting to sentimentAnalysis",e);
		}
	}
	
	
	private Timestamp formatTwitterDate(String date){
        try{
            simpleDateFormat.setLenient(true);
            Date s = simpleDateFormat.parse(date);
            Timestamp timestamp = new Timestamp(s.getTime());
            return timestamp;
        }
        catch(Exception e){
            logger.error("Exception while parsing twitter date: " + e);
            return null;
        }
    }
}
