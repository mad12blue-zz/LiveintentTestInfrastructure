package com.liveintent.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**********************************
 * RETRY ANALYZER FOR TESTNG TESTS *
 **********************************/
public class TestRetryAnalyzer implements IRetryAnalyzer {
	int counter = 1;
	int retryMaxLimit = 3;
	protected static final Logger logger = LogManager.getLogger(TestRetryAnalyzer.class);

	// Retry failed tests for 3 times
	public boolean retry(ITestResult result) {
		logger.debug("Getting data from workbook sheet");
		if (counter < retryMaxLimit) {
			counter++;
			return true;
		}
		return false;
	}
}