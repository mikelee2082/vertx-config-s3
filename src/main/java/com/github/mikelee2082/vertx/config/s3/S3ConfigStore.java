package com.github.mikelee2082.vertx.config.s3;

import java.net.URI;
import io.vertx.config.spi.ConfigStore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

public class S3ConfigStore implements ConfigStore {

	private Region region;
	private S3Client s3;
	private String key;
	private String bucket;

	public S3ConfigStore(Vertx vertx, JsonObject config) {
		try {
			key = config.getString("key");
			bucket = config.getString("bucket");
			region = Region.of(config.getString("region"));
			S3ClientBuilder builder = S3Client.builder().region(region);
            if (config.containsKey("access_key") && config.containsKey("secret_access_key")) {
                AwsCredentials creds = AwsBasicCredentials.create(config.getString("access_key"),
                    config.getString("secret_access_key"));
                AwsCredentialsProvider provider = StaticCredentialsProvider.create(creds);
                builder = builder.credentialsProvider(provider);
              }
			if (config.containsKey("endpoint_url")) {
				builder = builder.endpointOverride(new URI(config.getString("endpoint_url")));
			}
			s3 = builder.build();
		} catch (Exception e) {
			throw new Error(e);
		}		
	}

	public void get(Handler<AsyncResult<Buffer>> completionHandler) {
		try {
			GetObjectRequest getRequest = GetObjectRequest.builder().bucket(bucket).key(key).build();
			Buffer buffer = Buffer.buffer(s3.getObjectAsBytes(getRequest).asByteArray());
			completionHandler.handle(Future.succeededFuture(buffer));
		} catch (Exception e) {
			completionHandler.handle(Future.failedFuture(e));
		}
	}

}
