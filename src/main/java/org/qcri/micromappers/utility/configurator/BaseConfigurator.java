package org.qcri.micromappers.utility.configurator;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;
import org.qcri.micromappers.utility.configurator.exception.ConfigurationPropertyFileException;
import org.qcri.micromappers.utility.configurator.exception.ConfigurationPropertyNotRecognizedException;
import org.qcri.micromappers.utility.configurator.exception.ConfigurationPropertyNotSetException;
import org.qcri.micromappers.utility.configurator.exception.DirectoryNotWritableException;

public abstract class BaseConfigurator implements Configurator {

	private static final Logger LOGGER = Logger.getLogger(BaseConfigurator.class);

	private Map<String, String> propertyMap;

	@Override
	public void initProperties(String configLoadFileName,
			ConfigurationProperty[] configurationProperties)
			throws ConfigurationPropertyNotSetException,
			ConfigurationPropertyNotRecognizedException,
			ConfigurationPropertyFileException {
		LOGGER.info("Initializing Properties: " + configurationProperties
				+ " from file : " + configLoadFileName);
		propertyMap = ConfigurationPropertiesUtil.readConfigurations(
				configurationProperties, configLoadFileName);
	}

	@Override
	public String getProperty(ConfigurationProperty property) {
		return propertyMap.get(property.getName());
	}
	
	@Override
	public String getProperty(String propertyName) {
		return propertyMap.get(propertyName);
	}
	
	@Override
	public void setProperty(String property, String newValue) {
		if(propertyMap.containsKey(property)){
			propertyMap.put(property,newValue);
		}
		else{
			LOGGER.error("Encountered an unexpected property:'"
					+ property + "' with the value: "
					+ newValue);
			throw new ConfigurationPropertyNotRecognizedException(
					property, null);
		}
	}
	
	@Override
	public void directoryIsWritable(String propertyName) throws DirectoryNotWritableException{
		String directoryLocation = this.getProperty(propertyName);
		File f = new File(directoryLocation);
		if(!f.canWrite()) {
			LOGGER.info(propertyName+ " = " +directoryLocation+ " is not writable. Please verify if this is a valid writable directory.");
			throw new DirectoryNotWritableException(propertyName, directoryLocation);
		}
	}
}
