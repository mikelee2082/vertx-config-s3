# vertx-config-s3

![Maven Package](https://github.com/mikelee2082/vertx-config-s3/workflows/Maven%20Package/badge.svg?branch=v0.1.0)

Allow vertx applications to retrieve configuration from S3 object storage. Uses the standard vertx ConfigStoreOptions syntax with the "s3" type.

### Example

```
ConfigStoreOptions s3 = new ConfigStoreOptions()
        .setType("s3")
        .setFormat("json")
	.setConfig(new JsonObject()
		.put("key", "object_key")
		.put("bucket", "bucketName")
		.put("region", "us-west-2")
		.put("access_key", "my_aws_access_key")
		.put("secret_access_key", "my_aws_secret_access_key"));
ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(s3));

retriever.getConfig(responseHandler -> {
  if (responseHandler.succeeeded()) {
    JsonObject config = responseHandler.result();
  }
});
```
