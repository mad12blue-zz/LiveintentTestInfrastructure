package com.liveintent.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.liveintent.dataProvider.TestDataProviderSource;

/****************************************************************
 * READING TEST DATA FROM XLSX FILE AND STORING IT IN A 2D ARRAY *
 ****************************************************************/
public class ReadTestData {

	public HashMap<String, String> testDataMap = new HashMap<String, String>();
	protected static final Logger logger = LogManager.getLogger(ReadTestData.class);

	// Get data from xlsx and populate the testng data source
	public void populateDataSource(String filepath) {

		FileInputStream inputStream;

		try {
			logger.debug("laod the xlsx from " + filepath);
			inputStream = new FileInputStream(new File(filepath));

			Workbook workbook = new XSSFWorkbook(inputStream);

			int noOfSheets = workbook.getNumberOfSheets();

			logger.debug("Getting data from workbook sheet");
			for (int j = 0; j < noOfSheets; j++) {
				switch (workbook.getSheetName(j)) {
				case ("Routing"):
					TestDataProviderSource.routingTestData = readSheet(workbook.getSheet("Routing"));
					break;
				}
			}
			workbook.close();
			inputStream.close();

		} catch (Exception e) {
			logger.error(e);
		}
	}

	// Read data from specific sheet in xlsx
	private Object[][] readSheet(Sheet sheet) {
		Object[][] testData = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];
		int i = 0, x = 0, y = 0;

		Iterator<Row> iterator = sheet.iterator();

		logger.debug("Getting data from each row of sheet");
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			if (i == 0) {
				i++;
				continue;
			}

			logger.debug("Getting data from each cell of row");
			while (cellIterator.hasNext()) {

				Cell cell = cellIterator.next();
				cell.setCellType(Cell.CELL_TYPE_STRING);
				testData[x][y] = cell.toString();
				y++;
			}
			y = 0;
			x++;
		}
		x = 0;

		return testData;
	}
}