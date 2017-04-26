
package org.qcri.micromappers.utility.twitterCrawler;

import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.CollectionLabel;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.service.CollectionLabelService;
import org.qcri.micromappers.service.CollectionService;
import org.qcri.micromappers.service.TwitterCollectionService;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.GenericCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import twitter4j.TwitterException;

/**
 * Class to getting tweets based on username and optional time constraints
 * 
 * @author Jefferson Henrique
 */

@Service
public class TweetManager {

	private static Logger logger = Logger.getLogger(TweetManager.class);

	@Autowired
	private CollectionService collectionService;
	@Autowired
	private CollectionLabelService collectionLabelService;
	@Autowired
	private TwitterCollectionService twitterCollectionService;

	/**
	 * @param criteria An object of the class {@link TwitterCriteria} to indicate how tweets must be searched
	 * 
	 * @return A list of all tweets found
	 */
	@Async
	public void getTweets(CollectionTask task, TwitterCriteria criteria) {
		List<Tweet> results = new ArrayList<Tweet>();

		try {
			Collection collection = collectionService.getByCode(task.getCollectionCode());
			CollectionLabel collectionLabel = collectionLabelService.getByCollectionId(collection.getId());
			
			String refreshCursor = null;

			outerLace: while (GenericCache.getInstance().getTwitterConfig(task.getCollectionCode()) != null) {

				String urlResponse = getURLResponse(criteria.getUsername(), criteria.getSince(), criteria.getUntil(), 
						criteria.getQuerySearch(), criteria.getIncludeRetweet(), refreshCursor);

				JsonObject json = Json.createReader(new StringReader(urlResponse)).readObject();

				refreshCursor = json.getString("min_position");
				Document doc = Jsoup.parse((String) json.getString("items_html"));
				Elements tweetsElementList = doc.select("div.js-stream-tweet");

				if (tweetsElementList.size() == 0) {
					break;
				}

				for (Element tweetElement : tweetsElementList) {

					long dateMs = Long.valueOf(tweetElement.select("small.time span.js-short-timestamp").attr("data-time-ms"));
					String id = tweetElement.attr("data-tweet-id");

					//Will Uncomment the below part if need other fields from crawled tweet.

					/*String usernameTweet = tweetElement.select("span.username.js-action-profile-name b").text();
					String txt = tweetElement.select("p.js-tweet-text").text().replaceAll("[^\\u0000-\\uFFFF]", "");
					int retweets = Integer.valueOf(tweetElement.select("span.ProfileTweet-action--retweet span.ProfileTweet-actionCount").attr("data-tweet-stat-count").replaceAll(",", ""));
					int favorites = Integer.valueOf(tweetElement.select("span.ProfileTweet-action--favorite span.ProfileTweet-actionCount").attr("data-tweet-stat-count").replaceAll(",", ""));
					String permalink = tweetElement.attr("data-permalink-path");
					String geo = "";
					Elements geoElement = tweetElement.select("span.Tweet-geo");
					if (geoElement.size() > 0) {
						geo = geoElement.attr("title");
					}*/

					Tweet tweet = new Tweet();
					tweet.setId(id);
					tweet.setDate(new Date(dateMs));

					//Will Uncomment the below part if need other fields from crawled tweet.

					/*tweet.setPermalink("https://twitter.com"+permalink);
					tweet.setUsername(usernameTweet);
					tweet.setText(txt);
					tweet.setRetweets(retweets);
					tweet.setFavorites(favorites);
					tweet.setMentions(processTerms("(@\\w*)", txt));
					tweet.setHashtags(processTerms("(#\\w*)", txt));
					tweet.setGeo(geo);*/

					results.add(tweet);

					if (criteria.getMaxTweets() > 0 && results.size() >= criteria.getMaxTweets()) {
						break outerLace;
					}
				}

				if(results.size() >= Constants.MAX_TWEET_LOOKUP_COUNT_IN_ONE_HIT){
					List<Long> tweetIdsList = new ArrayList<Long>();
					
					for (Tweet result : results.subList(0, Constants.MAX_TWEET_LOOKUP_COUNT_IN_ONE_HIT)) {
						try{
							tweetIdsList.add(Long.parseLong(result.getId()));
						}catch(Exception e){
							logger.warn("Exception while parsing tweet");
						}
					}
								
					try{
						List<JsonObject> fullTweets = twitterCollectionService.searchTweets(tweetIdsList, task.getAccessToken(), task.getAccessTokenSecret());
						twitterCollectionService.persistTweets(collection, fullTweets, collectionLabel);
						task.setTwitterLastExecutionTime(results.get(Constants.MAX_TWEET_LOOKUP_COUNT_IN_ONE_HIT - 1).getDate());

						results = results.subList(Constants.MAX_TWEET_LOOKUP_COUNT_IN_ONE_HIT, results.size());
					}catch (TwitterException e) {
						if(e.getErrorCode() == 88){
							logger.warn("Twitter API rate limited. Twitter Search going to sleep for 5 minutes");
							Thread.sleep(Constants.ONE_HOUR_IN_MILLISECS/12);
						}
					}
				}

				//Delay between 2 crawl hits 
				Thread.sleep(Constants.TWITTER_SEARCH_API_DELAY_IN_MILLISECS);
			}
			
			while(results.size() > 0){
				List<Long> tweetIdsList = new ArrayList<Long>();
				for (Tweet result : results.subList(0, Math.min(Constants.MAX_TWEET_LOOKUP_COUNT_IN_ONE_HIT, results.size()))) {
					try{
						tweetIdsList.add(Long.parseLong(result.getId()));
					}catch(Exception e){
						logger.warn("Exception while parsing tweet");
					}
				}
				
				//tweetIdsList = results.subList(0, Math.min(Constants.MAX_TWEET_LOOKUP_COUNT_IN_ONE_HIT, results.size())).stream().map(x -> Long.parseLong(x.getId())).collect(Collectors.toList());
				
				try{
					List<JsonObject> fullTweets = twitterCollectionService.searchTweets(tweetIdsList, task.getAccessToken(), task.getAccessTokenSecret());
					twitterCollectionService.persistTweets(collection, fullTweets, collectionLabel);
					task.setTwitterLastExecutionTime(results.get(Math.min(Constants.MAX_TWEET_LOOKUP_COUNT_IN_ONE_HIT, results.size()) - 1).getDate());

					results = results.subList(Math.min(Constants.MAX_TWEET_LOOKUP_COUNT_IN_ONE_HIT, results.size()), results.size());
				}catch (TwitterException e) {
					if(e.getErrorCode() == 88){
						logger.warn("Twitter API rate limited. Twitter Search going to sleep for 5 minutes");
						Thread.sleep(Constants.ONE_HOUR_IN_MILLISECS/12);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception while crawling twitter.", e);
		}
		logger.info("Twitter crawling for collection code : "+ task.getCollectionCode() + " is finished.");
		//return results;
	}


	/**
	 * @param userName A specific username (without @)
	 * @param since Lower bound date (yyyy-mm-dd)
	 * @param until Upper bound date (yyyy-mm-dd)
	 * @param scrollCursor (Parameter used by Twitter to do pagination of results)
	 * @return JSON response used by Twitter to build its results
	 * @throws Exception
	 */
	private String getURLResponse(String userName, String since, String until, String querySearch, Boolean includeRetweets, String scrollCursor) throws Exception {
		String appendQuery = "";

		if (userName != null) {
			appendQuery += "from:"+userName;
		}
		if (since != null) {
			appendQuery += " since:"+since;
			appendQuery = appendQuery.trim();
		}
		if (until != null) {
			appendQuery += " until:"+until;
			appendQuery = appendQuery.trim();
		}
		if (querySearch != null) {
			appendQuery += " "+formatQuerySearch(querySearch.trim());
			appendQuery = appendQuery.trim();
		}
		if(includeRetweets !=null && includeRetweets){
			appendQuery += " include:retweets";
			appendQuery = appendQuery.trim();
		}

		String url = String.format("https://twitter.com/i/search/timeline?f=realtime&q=%s&src=typd&max_position=%s", URLEncoder.encode(appendQuery, "UTF-8"), scrollCursor);
		//String url = String.format("https://twitter.com/search?f=realtime&q=%s&src=typd&max_position=%s", URLEncoder.encode(appendQuery, "UTF-8"), scrollCursor);

		return getPageResponse(url);
	}


	private static String formatQuerySearch(String querySearch){
		String[] split = querySearch.split(",");
		StringBuilder stringBuilder = new StringBuilder();

		int i= 0;
		for(i= 0; i< split.length-1; i++){
			if(split[i].trim().contains(" ")){
				String aWord = "\""+ split[i].trim() + "\"";
				stringBuilder.append(aWord + " OR ");
			}
			else{
				stringBuilder.append(split[i].trim() + " OR ");
			}
		}
		if(split.length > 0){
			stringBuilder.append(split[i].trim());
		}

		return stringBuilder.toString().trim();
	}


	private String getPageResponse(String url) throws Exception{
		RequestConfig globalConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.DEFAULT)
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				.setDefaultRequestConfig(globalConfig)
				.build();
		RequestConfig localConfig = RequestConfig.copy(globalConfig)
				.setCookieSpec(CookieSpecs.STANDARD)
				.build();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(localConfig);

		HttpEntity resp = httpClient.execute(httpGet).getEntity();
		return EntityUtils.toString(resp);
	}


	//Uncomment the below method if required to process tweetText from the crawled doc

	/*private String processTerms(String patternS, String tweetText) {
		StringBuilder sb = new StringBuilder();
		Matcher matcher = Pattern.compile(patternS).matcher(tweetText);
		while (matcher.find()) {
			sb.append(matcher.group());
			sb.append(" ");
		}
		return sb.toString().trim();
	}*/

}