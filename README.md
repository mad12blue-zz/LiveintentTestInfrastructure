# LiveintentTestInfrastructure
API Test Automation framework designed using Java, Maven, Rest Assured, TestNG, Log4j2.

--------------------------------------------------------------------------------------------
Getting the code:
--------------------------------------------------------------------------------------------
`git clone https://github.com/mad12blue/LiveintentTestInfrastructure.git` (Clone the test suite)

--------------------------------------------------------------------------------------------
Configuration:
--------------------------------------------------------------------------------------------
In the file `/properties/config.properties` the below properties can be configured

- ROUTING_URL = http://localhost:9000/
- ROUTE_PATH = route
- KINESIS_URL = http://localhost:4568/
- AWS_ACCESS_KEY_ID=foo
- AWS_SECRET_ACCESS_KEY=bar
- STREAM_ODD = li-stream-odd
- STREAM_EVEN = li-stream-even
- HEADER_NAME = X-Transaction-Id

--------------------------------------------------------------------------------------------
Running the test suite:
--------------------------------------------------------------------------------------------
Pre-condition: make sure the service under test is up and running in the localhost

1. `cd LiveintentTestInfrastructure` (navigate to the 'LiveintentTestInfrastructure' folder)

2. `mvn install` (Install dependencies)

3. `mvn test` (Execute the tests)

--------------------------------------------------------------------------------------------
Additional Info:
--------------------------------------------------------------------------------------------
* Tests can be found in `/src/test/java/com/liveintent/test/` folder
* Test data can be found in `/data/` folder
* Utility methods can be found in `/src/test/java/com/liveintent/Utilities/` folder
* Rest helper can be found in `/src/test/java/com/liveintent/helper/` folder
* Configuration properties can be found in `/properties/config.properties` file
* Logs can be found in `/log/` folder
* Report can be found in `/test-output/index.html` file
