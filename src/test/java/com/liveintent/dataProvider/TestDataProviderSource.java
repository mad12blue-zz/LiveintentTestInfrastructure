package com.liveintent.dataProvider;

import org.testng.annotations.DataProvider;

/*******************************************
 * TEST DATA PROVIDER SOURCE FOR TESNG TEST *
 *******************************************/
public class TestDataProviderSource {

	public static Object[][] routingTestData;

	@DataProvider
	// Routing test data provider
	public static Object[][] routingDataProvider() {
		return routingTestData;
	}
}