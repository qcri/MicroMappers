package org.qcri.micromappers.utility.configurator;

import org.qcri.micromappers.utility.configurator.exception.ConfigurationPropertyFileException;
import org.qcri.micromappers.utility.configurator.exception.ConfigurationPropertyNotRecognizedException;
import org.qcri.micromappers.utility.configurator.exception.ConfigurationPropertyNotSetException;
import org.qcri.micromappers.utility.configurator.exception.DirectoryNotWritableException;

public interface Configurator {

	public void initProperties(String configLoadFileName,
			ConfigurationProperty[] configurationProperties)
			throws ConfigurationPropertyNotSetException,
			ConfigurationPropertyNotRecognizedException,
			ConfigurationPropertyFileException;

	public String getProperty(ConfigurationProperty property);
	public String getProperty(String propertyName);
	public void setProperty(String property, String newValue);
	public void directoryIsWritable(String propertyName) throws DirectoryNotWritableException;
}
