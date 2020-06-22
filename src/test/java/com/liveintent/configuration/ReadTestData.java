package com.liveintent.configuration;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.liveintent.dataProvider.TestDataProviderSource;

/****************************************************************
 * READING TEST DATA FROM CSV FILE AND STORING IT IN A 2D ARRAY *
 ****************************************************************/
public class ReadTestData {

	public HashMap<String, String> testDataMap = new HashMap<String, String>();
	protected static final Logger logger = LogManager.getLogger(ReadTestData.class);

	// Get data from csv and populate the testng data source
	@SuppressWarnings("deprecation")
	public void populateDataSource(String filepath) {
		int i = 0;
		try {
			logger.debug("laod the csv from " + filepath);
			String thisLine;
			FileInputStream fis = new FileInputStream(filepath);
			DataInputStream myInput = new DataInputStream(fis);

			List<String[]> lines = new ArrayList<String[]>();
			while ((thisLine = myInput.readLine()) != null) {
				if (i != 0)
					lines.add(thisLine.split(","));
				i++;
			}
			// convert our list to a Object array.
			Object[][] array = new String[lines.size()][0];
			TestDataProviderSource.routingTestData = lines.toArray(array);

			myInput.close();
			fis.close();

		} catch (Exception e) {
			logger.error(e);
		}
	}
}