package com.liveintent.helper;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.restassured.response.Response;

/*****************************************
 * HELPER FUNCTION FOR ALL REST FUNCTIONS *
 *****************************************/
public class RestHelper {

	protected static final Logger logger = LogManager.getLogger(RestHelper.class);

	// Send get request with variables
	public Response sendGetRequest(String url) {
		logger.debug("Send GET request with url " + url);
		return get(url);
	}

	// Send post request with variables
	public Response sendPostRequest(String url, String body) {
		logger.debug("Send POST request with url " + url + " and body " + body);
		return given().body(body).post(url);
	}
}