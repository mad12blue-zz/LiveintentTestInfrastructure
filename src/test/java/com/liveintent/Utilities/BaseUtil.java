package com.liveintent.Utilities;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.restassured.response.Response;
import com.liveintent.configuration.ReadProperties;
import com.liveintent.helper.RestHelper;
import com.liveintent.variables.Var;

/****************************************************************
 * BASE UTILTIY WITH COMMON METHODS SHARING WITH OTHER UTILITIES *
 ****************************************************************/
public class BaseUtil {

	private String configProp = "properties/config.properties";
	protected RestHelper restHelper;
	protected static final Logger logger = LogManager.getLogger(BaseUtil.class);

	// Initializing property file
	public BaseUtil() {
		logger.info("Reading Properties from " + configProp);
		Var.rcp = new ReadProperties();
		Var.rcp.readProp(configProp);
	}

	// Verify status code in response
	public void verifyStatusCode(String statusCode, Response response) {
		logger.info("Assert Status Code in Response");
		assertEquals(response.getStatusCode(), Integer.parseInt(statusCode));
	}

	// Verify the header in response
	public void verifyHeaderWithKey(Response response, String headerKey) {
		logger.info("Assert header in Response");
		assertTrue(response.getHeaders().hasHeaderWithName(headerKey));
		assertNotNull(response.getHeader(headerKey));
	}
}