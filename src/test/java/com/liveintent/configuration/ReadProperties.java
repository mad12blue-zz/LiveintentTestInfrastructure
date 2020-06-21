package com.liveintent.configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**************************************************
 * READING CONFIGURATION DATA FROM PROPERTIES FILE *
 **************************************************/
public class ReadProperties {

	public Properties prop;
	protected static final Logger logger = LogManager.getLogger(ReadProperties.class);

	// Read data form properties file
	public void readProp(String filePath) {

		prop = new Properties();
		InputStream configInput = null;

		try {
			logger.debug("Getting config file from path " + filePath);
			configInput = new FileInputStream(filePath);
			prop.load(configInput);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}