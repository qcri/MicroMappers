package org.qcri.micromappers.utility.configurator;

import org.qcri.micromappers.exception.ConfigurationPropertyFileException;
import org.qcri.micromappers.exception.ConfigurationPropertyNotRecognizedException;
import org.qcri.micromappers.exception.ConfigurationPropertyNotSetException;
import org.qcri.micromappers.exception.DirectoryNotWritableException;

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
