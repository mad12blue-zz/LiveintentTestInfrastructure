package com.liveintent.test;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import com.liveintent.Utilities.KinesisUtil;
import com.liveintent.Utilities.RoutingUtil;
import com.liveintent.dataProvider.TestDataProviderSource;
import io.restassured.response.Response;

/***********************************************
 * ROUTING TEST TO ROUTE DATA TO KINESIS STREAM *
 ***********************************************/
public class RoutingTest extends BaseTest {

	RoutingUtil routingUtil;
	KinesisUtil kinesisUtil;
	Response response;
	String seed;

	@BeforeClass
	// Initialize utility objects
	public void initialize() {
		routingUtil = new RoutingUtil();
		kinesisUtil = new KinesisUtil();
	}

	@Test(dataProvider = "routingDataProvider", dataProviderClass = TestDataProviderSource.class, description = "Route data to the correct stream based on seed")
	// Route data do different streams based on seed
	public void routingTest(String seed, String statusCode) {

		logger.info("Route with " + seed);
		response = routingUtil.route(seed);

		logger.info("Verify routing with seed " + seed + ", Results in status code " + statusCode);
		routingUtil.verifyStatusCode(statusCode, response);

		if (response.getStatusCode() == 200) {
			logger.info("Verify X-Transaction-Id header is present in response");
			routingUtil.verifyHeaderWithKey(response, "X-Transaction-Id");

			logger.info("Verify data routed to the correct stream");
			kinesisUtil.verifyRecordAddedToCorrectStream(response, seed);
		}
	}
}