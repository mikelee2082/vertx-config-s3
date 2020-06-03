package nz.silence.vertx.config.s3;

import java.net.URI;
import java.net.URISyntaxException;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

public class S3ConfigStore implements ConfigStore {
	
	private Region region;
	private S3Client s3;
	private String key;
	private String bucket;
	
	public S3ConfigStore(Vertx vertx, JsonObject config) {
		key = config.getString("key");
		bucket = config.getString("bucket");
		region = Region.of(config.getString("region"));
		S3ClientBuilder builder = S3Client.builder().region(region);
		if (config.containsKey("endpoint_url")) {
			try {
				builder = builder.endpointOverride(new URI(config.getString("endpoint_url")));
			} catch (URISyntaxException e) {
				throw new Error(e);
			}
		}
	}

	public void get(Handler<AsyncResult<Buffer>> completionHandler) {
	}

}
