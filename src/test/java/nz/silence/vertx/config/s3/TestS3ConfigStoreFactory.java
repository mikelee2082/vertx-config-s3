package nz.silence.vertx.config.s3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import software.amazon.awssdk.regions.Region;

@ExtendWith(VertxExtension.class)
class TestS3ConfigStoreFactory {

	@Test
	public void testInit(Vertx vertx, VertxTestContext context) {
		ConfigStoreOptions s3 = new ConfigStoreOptions()
				.setType("s3")
				.setFormat("json")
				.setConfig(new JsonObject()
						.put("key", "a key")
						.put("bucket", "bucket")
						.put("region", Region.US_WEST_2.toString())
						.put("access_key", "access_key")
						.put("secret_access_key", "secret_access_key"));				
		ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(s3));
		assertNotNull(retriever);
		context.completeNow();
	}

}
