
package org.qcri.micromappers.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.twitterCrawler.TweetManager;
import org.qcri.micromappers.utility.twitterCrawler.TwitterCriteria;
import org.springframework.stereotype.Service;

@Service
public class TwitterCollectionCrawler
{	
	@Inject
	private TweetManager tweetManager;

	private static Logger logger = Logger.getLogger(TwitterCollectionCrawler.class);

	/** This method is used to crawl the older tweets by criteria.
	 * @param collectionTask
	 */
	public void crawlTweets(CollectionTask collectionTask){
		DateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT);
		if(collectionTask != null){
			if(collectionTask.getTwitterUntilDate() != null){
				TwitterCriteria criteria = TwitterCriteria.create();

				criteria.setUntil(dateFormatter.format(collectionTask.getLastExecutionTime() != null ? collectionTask.getLastExecutionTime() : collectionTask.getTwitterUntilDate()));

				if(collectionTask.getTwitterSinceDate() != null){
					criteria.setSince(dateFormatter.format(collectionTask.getTwitterSinceDate()));
				}

				if(StringUtils.isNotBlank(collectionTask.getToTrack())){
					criteria.setQuerySearch(collectionTask.getToTrack());
				}

				logger.info("Starting the twitter crawler for collection Code: "+collectionTask.getCollectionCode() + "with query : "+ criteria.toString());
				tweetManager.getTweets(collectionTask, criteria);
			}
		}
	}

}
