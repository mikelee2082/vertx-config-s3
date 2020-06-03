package nz.silence.vertx.config.s3;

import io.vertx.config.spi.ConfigStore;
import io.vertx.config.spi.ConfigStoreFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class S3ConfigStoreFactory implements ConfigStoreFactory {
	
	private static final String NAME = "s3";

	public String name() {
		return NAME;
	}

	public ConfigStore create(Vertx vertx, JsonObject configuration) {
		return new S3ConfigStore(vertx, configuration);
	}

}
