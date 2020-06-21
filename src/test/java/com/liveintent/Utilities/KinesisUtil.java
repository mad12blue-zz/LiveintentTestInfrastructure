package com.liveintent.Utilities;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import java.math.BigInteger;
import java.util.List;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.GetRecordsRequest;
import com.amazonaws.services.kinesis.model.GetRecordsResult;
import com.amazonaws.services.kinesis.model.GetShardIteratorRequest;
import com.amazonaws.services.kinesis.model.GetShardIteratorResult;
import com.amazonaws.services.kinesis.model.ListShardsRequest;
import com.amazonaws.services.kinesis.model.ListShardsResult;
import com.amazonaws.services.kinesis.model.Record;
import com.amazonaws.services.kinesis.model.Shard;
import com.liveintent.variables.Var;
import io.restassured.response.Response;
import org.json.JSONObject;

/**************************************************************
 * KINESIS UTIL TO SUPPORT WITH KINESIS STREAM RELATED ACTIONS *
 **************************************************************/
public class KinesisUtil extends BaseUtil {

	private AmazonKinesis client;
	private EndpointConfiguration localEndpointConfig;
	private AWSCredentialsProvider credentialsProvider;
	private static String sequenceNumberLiStreamOdd = "";
	private static String sequenceNumberLiStreamEven = "";
	private BigInteger bigInt;

	// Initialize the kinesis client with config properties
	public KinesisUtil() {
		System.setProperty("com.amazonaws.sdk.disableCbor", "true");

		logger.debug("Set the Kinesis url and the region");
		localEndpointConfig = new EndpointConfiguration(Var.rcp.prop.getProperty("KINESIS_URL"),
				Regions.US_EAST_1.getName());

		logger.debug("Set the Kinesis access key and secret");
		credentialsProvider = new AWSCredentialsProvider() {
			@Override
			public AWSCredentials getCredentials() {
				return new BasicAWSCredentials(Var.rcp.prop.getProperty("AWS_ACCESS_KEY_ID"),
						Var.rcp.prop.getProperty("AWS_SECRET_ACCESS_KEY"));
			}

			@Override
			public void refresh() {
			}
		};

		logger.debug("Build kinesis client");
		client = AmazonKinesisClientBuilder.standard().withEndpointConfiguration(localEndpointConfig)
				.withCredentials(credentialsProvider).build();
	}

	// Get all shards from a stream
	private List<Shard> getShards(String streamName) {
		ListShardsRequest request = new ListShardsRequest();
		request.setStreamName(streamName);
		ListShardsResult response = null;

		try {
			logger.debug("List all shards in the stream " + streamName);
			response = client.listShards(request);
		} catch (Exception e) {
			logger.error(e);
		}

		return response.getShards();
	}

	// Get latest shard from the list of shards
	private Shard getLatestShard(List<Shard> shards) {
		logger.debug("Get latest shard from the list of shards");
		return shards.get(shards.size() - 1);
	}

	// Get all records from a shard
	private List<Record> getRecordsFromShard(String streamName, Shard shard) {
		GetShardIteratorRequest getShardIteratorRequest = new GetShardIteratorRequest();
		getShardIteratorRequest.setStreamName(streamName);
		getShardIteratorRequest.setShardId(shard.getShardId());
		getShardIteratorRequest = setShardIteratorType(getShardIteratorRequest, streamName);

		GetShardIteratorResult getShardIteratorResult = client.getShardIterator(getShardIteratorRequest);
		GetRecordsRequest getRecordsRequest = new GetRecordsRequest();

		logger.debug("Set shard iterator type");
		getRecordsRequest.setShardIterator(getShardIteratorResult.getShardIterator());
		getRecordsRequest.setLimit(10000);

		logger.debug("Get records from shard " + shard);
		GetRecordsResult getRecordsResult = client.getRecords(getRecordsRequest);

		return getRecordsResult.getRecords();
	}

	// Set shard iterator type based on sequence number
	private GetShardIteratorRequest setShardIteratorType(GetShardIteratorRequest getShardIteratorRequest,
			String streamName) {
		if ((sequenceNumberLiStreamOdd == "") || (sequenceNumberLiStreamEven == "")) {
			logger.debug("Set shard iterator type to TRIM_HORIZON");
			getShardIteratorRequest.setShardIteratorType("TRIM_HORIZON");
		} else if (streamName.contains("odd")) {
			logger.debug("Set shard iterator type to AFTER_SEQUENCE_NUMBER for odd stream shard");
			getShardIteratorRequest.setShardIteratorType("AFTER_SEQUENCE_NUMBER");
			getShardIteratorRequest.setStartingSequenceNumber(sequenceNumberLiStreamOdd);
		} else {
			logger.debug("Set shard iterator type to AFTER_SEQUENCE_NUMBER for even stream shard");
			getShardIteratorRequest.setShardIteratorType("AFTER_SEQUENCE_NUMBER");
			getShardIteratorRequest.setStartingSequenceNumber(sequenceNumberLiStreamEven);
		}

		return getShardIteratorRequest;
	}

	// Get latest record from the list of records
	private JSONObject getLatestRecord(List<Record> records, String streamName) {
		int recordSize;
		String sequenceNumber;

		try {
			recordSize = records.size();
			sequenceNumber = records.get(recordSize - 1).getSequenceNumber();
			sequenceNumber = (streamName.contains("odd")) ? (sequenceNumberLiStreamOdd = sequenceNumber)
					: (sequenceNumberLiStreamEven = sequenceNumber);
			logger.debug("Get single record from the list of records");
			return new JSONObject(records.get(recordSize - 1));
		} catch (IndexOutOfBoundsException ie) {
			return null;
		}
	}

	// Get partition key from the record
	private String getPartitionKey(JSONObject record) {
		try {
			logger.debug("Get partition key from the record");
			return record.get("partitionKey").toString();
		} catch (NullPointerException ne) {
			return null;
		}
	}

	// Get partition key from the latest record of the latest shard in the stream
	private String getLatestPartitionKey(String streamName) {
		logger.debug("Get shard list");
		List<Shard> shardList = getShards(streamName);
		logger.debug("Get latest shard");
		Shard latestShard = getLatestShard(shardList);

		logger.debug("Get records");
		List<Record> records = getRecordsFromShard(streamName, latestShard);
		logger.debug("Get latest record");
		JSONObject record = getLatestRecord(records, streamName);

		logger.debug("Get partition key");
		return getPartitionKey(record);
	}

	// Check if the seed is even
	private boolean isEven(String num) {
		bigInt = new BigInteger(num);
		logger.debug("Check if the seed is even " + num);
		return bigInt.mod(new BigInteger("2")).equals(BigInteger.ZERO);
	}

	// Verify if the record is added to the correct stream
	public void verifyRecordAddedToCorrectStream(Response response, String number) {

		String header = Var.rcp.prop.getProperty("HEADER_NAME");
		String evenStream = Var.rcp.prop.getProperty("STREAM_EVEN");
		String oddStream = Var.rcp.prop.getProperty("STREAM_ODD");

		if (isEven(number)) {
			logger.info("Assert if record is created in even stream");
			assertEquals(response.getHeader(header), getLatestPartitionKey(evenStream));
			logger.debug("Assert if record is not created in odd stream");
			assertNotEquals(response.getHeader(header), getLatestPartitionKey(oddStream));
		} else {
			logger.debug("Assert if record is created in odd stream");
			assertEquals(response.getHeader(header), getLatestPartitionKey(oddStream));
			logger.info("Assert if record is not created in even stream");
			assertNotEquals(response.getHeader(header), getLatestPartitionKey(evenStream));
		}
	}
}