package com.liveintent.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.liveintent.configuration.ReadTestData;
import com.liveintent.variables.Var;

/***********************************************
 * BASE TEST WITH COMMON OPERATIONS IN ANY TEST *
 ***********************************************/
public class BaseTest {

	String configProp = "properties/config.properties";
	String testDataCsv = "data/test-data.csv";

	protected static final Logger logger = LogManager.getLogger(BaseTest.class);

	@BeforeSuite(alwaysRun = true)
	// Read config data and populate test data
	protected void SuiteLevelSetup() {
		logger.info("**-------------------------------START TEST-------------------------------**");
		logger.debug("Reading Test data from " + testDataCsv);
		Var.rtd = new ReadTestData();
		Var.rtd.populateDataSource(testDataCsv);
	}

	@AfterSuite(alwaysRun = true)
	// Read config data and populate test data
	protected void SuiteLevelCleanup() {
		logger.info("**--------------------------------END TEST--------------------------------**");
	}
}