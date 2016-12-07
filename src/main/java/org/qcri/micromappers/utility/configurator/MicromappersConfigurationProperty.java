package org.qcri.micromappers.utility.configurator;

/**
 * @author Kushal
 * 
 *         Enum containing all the property keys required by the Micromappers.
 *
 */

public enum MicromappersConfigurationProperty implements ConfigurationProperty {
	
	GDELT_IMAGE_PATH("GDELT_IMAGE_PATH"),
	GDELT_ARTICLE_PATH("GDELT_ARTICLE_PATH"),
	GDELT_LAST_UPDATE_URL("GDELT_LAST_UPDATE_URL"),
	GDELT_DOWNLOADED_LAST_UPDATE_PATH("GDELT_DOWNLOADED_LAST_UPDATE_PATH"),
	GDELT_JSON_UPDATE_PATH("GDELT_JSON_UPDATE_PATH");
	
	private final String configurationProperty;

	private MicromappersConfigurationProperty(String property) {
		configurationProperty = property;
	}

	@Override
	public String getName() {
		return this.configurationProperty;
	}

}
