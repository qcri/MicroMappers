package org.qcri.micromappers.utility.configurator;

/**
 * @author Kushal
 * 
 *         Enum containing all the property keys required by the Micromappers.
 *
 */

public enum MicromappersConfigurationProperty implements ConfigurationProperty {
	TWITTER_APP_KEY("TWITTER_APP_KEY"),
	TWITTER_APP_SECRET("TWITTER_APP_SECRET"),
	FACEBOOK_APP_KEY("FACEBOOK_APP_KEY"),
	FACEBOOK_APP_SECRET("FACEBOOK_APP_SECRET"),
	GOOGLE_APP_KEY("GOOGLE_APP_KEY"),
	GOOGLE_APP_SECRET("GOOGLE_APP_SECRET"),
	GDELT_IMAGE_PATH("GDELT_IMAGE_PATH"),
	GDELT_ARTICLE_PATH("GDELT_ARTICLE_PATH"),
	GDELT_LAST_UPDATE_URL("GDELT_LAST_UPDATE_URL"),
	GDELT_DOWNLOADED_LAST_UPDATE_PATH("GDELT_DOWNLOADED_LAST_UPDATE_PATH"),
	GDELT_JSON_UPDATE_PATH("GDELT_JSON_UPDATE_PATH"),
	Feed_PATH("Feed_PATH"),
	SNOPES_COM_BASE_URL("SNOPES_COM_BASE_URL"),
	SNOPES_COM_FACT_CHECK_URL("SNOPES_COM_FACT_CHECK_URL"),
	SNOPES_COM_NEWS_URL("SNOPES_COM_NEWS_URL");
	
	private final String configurationProperty;

	private MicromappersConfigurationProperty(String property) {
		configurationProperty = property;
	}

	@Override
	public String getName() {
		return this.configurationProperty;
	}

}
