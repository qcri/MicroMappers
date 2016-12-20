package org.qcri.micromappers.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.DataFeed;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.DataFeedRepository;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.Util;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.springframework.stereotype.Service;

@Service
public class DataFeedService
{
	private static Logger logger = Logger.getLogger(DataFeedService.class);
	private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();

	@Inject
	private DataFeedRepository dataFeedRepository;

	@Inject
	private Util util;

	private DataFeed create(DataFeed dataFeed)
	{
		try{
			return dataFeedRepository.save(dataFeed);
		}catch (Exception e) {
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
	public DataFeed findByProviderAndFeedId(CollectionType provider, Long feedId)
	{
		try{
			return dataFeedRepository.findByProviderAndFeedIdAndParentFeedIsNull(provider, feedId);
		}catch (Exception e) {
			logger.error("Error while fetching dataFeed by provider & feedId", e);
			throw new MicromappersServiceException("Error while fetching dataFeed by provider & feedId", e);
		}
	}

	public DataFeed persistToDbAndFile(DataFeed dataFeed, String feed)
	{
		try{
			DataFeed parentFeed = findByProviderAndFeedId(dataFeed.getProvider(), dataFeed.getFeedId());
			if(parentFeed != null){
				//				dataFeed.setFeedId(null);
				dataFeed.setParentFeed(parentFeed);
			}else{
				persistFeedToFile(dataFeed, feed);
			}
			return create(dataFeed);

		}catch (MicromappersServiceException e) {
			logger.error(e.getMessage(), e);
			throw new MicromappersServiceException(e.getMessage(), e);
		}
	}

	private void persistFeedToFile(DataFeed dataFeed, String feed) {
		Path parentPath = Paths.get(configProperties.getProperty(MicromappersConfigurationProperty.Feed_PATH), 
				dataFeed.getCollection().getCode(), dataFeed.getProvider().toString());

		if(util.createDirectories(parentPath)){
			Path filePath = Paths.get(parentPath.toString(), dataFeed.getFeedId().toString());
			util.writeToFile(filePath, feed);
		}

	}
}
