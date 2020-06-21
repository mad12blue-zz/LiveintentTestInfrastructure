package com.liveintent.Utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.liveintent.helper.RestHelper;
import com.liveintent.variables.Var;

/*******************************************************
 * ROUTING UTIL TO SUPPORT WITH ROUTING RELATED ACTIONS *
 *******************************************************/
public class RoutingUtil extends BaseUtil {

	private RestHelper restHelper;

	// Initialize the base url and base path
	public RoutingUtil() {
		logger.info("Get the rout url");
		RestAssured.baseURI = Var.rcp.prop.getProperty("ROUTING_URL");
		RestAssured.basePath = Var.rcp.prop.getProperty("ROUTE_PATH");
		restHelper = new RestHelper();
	}

	// Route data with seed
	public Response route(String seed) {
		logger.info("Send GET Request with seed " + seed);
		return restHelper.sendGetRequest(seed);
	}
}