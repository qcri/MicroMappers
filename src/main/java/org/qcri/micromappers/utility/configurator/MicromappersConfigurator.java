package org.qcri.micromappers.utility.configurator;

import org.qcri.micromappers.exception.ConfigurationPropertyFileException;
import org.qcri.micromappers.exception.ConfigurationPropertyNotRecognizedException;
import org.qcri.micromappers.exception.ConfigurationPropertyNotSetException;

public class MicromappersConfigurator extends BaseConfigurator {
	
	public static final String configLoadFileName = "config.properties";

	private static final MicromappersConfigurator instance = new MicromappersConfigurator();

	private MicromappersConfigurator() {
		this.initProperties(MicromappersConfigurator.configLoadFileName, MicromappersConfigurationProperty.values());
		this.directoryIsWritable(MicromappersConfigurationProperty.GDELT_ARTICLE_PATH.getName());
		this.directoryIsWritable(MicromappersConfigurationProperty.GDELT_DOWNLOADED_LAST_UPDATE_PATH.getName());
		this.directoryIsWritable(MicromappersConfigurationProperty.GDELT_IMAGE_PATH.getName());
		this.directoryIsWritable(MicromappersConfigurationProperty.GDELT_JSON_UPDATE_PATH.getName());
		this.directoryIsWritable(MicromappersConfigurationProperty.Feed_PATH.getName());
		this.directoryIsWritable(MicromappersConfigurationProperty.IMAGE_CLASSIFIER_PATH.getName());
	}

	public static MicromappersConfigurator getInstance()
			throws ConfigurationPropertyNotSetException,
			ConfigurationPropertyNotRecognizedException,
			ConfigurationPropertyFileException {
		return instance;
	}

}
