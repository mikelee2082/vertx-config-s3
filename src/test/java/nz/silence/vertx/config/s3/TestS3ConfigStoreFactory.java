package nz.silence.vertx.config.s3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.URI;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.testing.s3mock.S3MockApplication;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ExtendWith(VertxExtension.class)
class TestS3ConfigStoreFactory {
	
	private static S3MockApplication s3Mock;	

	@Test
	public void testInit(Vertx vertx, VertxTestContext context) {
		ConfigStoreOptions s3 = new ConfigStoreOptions().setType("s3").setFormat("json")
				.setConfig(new JsonObject()
						.put("key", "key")
						.put("bucket", "bucket")
						.put("region", Region.US_WEST_2.toString())
						.put("access_key", "access_key")
						.put("secret_access_key", "secret_access_key")
						.put("endpoint_url", "http://localhost:9000"));
		ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(s3));
		retriever.getConfig(context.succeeding(handler -> {
			assertEquals("hello", handler.getString("message"));
			context.completeNow();
		}));
		assertNotNull(retriever);
	}
	
	@BeforeAll
	public static void startMockS3Server() {
		try {
			File uploadFile = new File("src/test/resources/test.json");
			s3Mock = S3MockApplication.start("--server.port=9001", "--http.port=9000");
			AwsCredentialsProvider provider = StaticCredentialsProvider
					.create(AwsBasicCredentials.create("key", "secret"));
			S3Client client = S3Client.builder().credentialsProvider(provider)
					.endpointOverride(new URI("http://localhost:9000")).region(Region.US_WEST_2).build();
			client.createBucket(CreateBucketRequest.builder().bucket("bucket").build());
			client.putObject(PutObjectRequest.builder().bucket("bucket").key("key").build(),
					RequestBody.fromFile(uploadFile));
			client.close();
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	@AfterAll
	public static void shutdown() {
		s3Mock.stop();
	}

}
