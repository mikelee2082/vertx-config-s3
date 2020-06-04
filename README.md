# vertx-config-s3

![Maven Package](https://github.com/mikelee2082/vertx-config-s3/workflows/Maven%20Package/badge.svg?branch=v0.1.0)

Allow vertx applications to retrieve configuration from S3 object storage. The library is an extension to the Vertx ConfigStoreFactory to allow configuration to be retrieved from a file kept in an S3 object store. For more information, see the [vertx-config documentation](https://vertx.io/docs/vertx-config/java/).

## Maven

```
<dependency>
  <groupId>com.github.mikelee2082</groupId>
  <artifactId>vertx-config-s3</artifactId>
  <version>0.1.0</version>
</dependency>
```
Then run:

```
mvn install
```

## Usage

Pass "s3" to the setType method when creating ConfigStoreOptions. The configuration store also requires standard S3 parameters to be passed, including:

- the object key
- the bucket where the object can be found
- the region
- an AWS access key
- an AWS secret access key

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

Optionally, the endpoint URL can be overridden to point at another S3 interface, such as one provided by a locally-hosted Ceph cluster or Minio cluster.

```
ConfigStoreOptions s3 = new ConfigStoreOptions()
        .setType("s3")
        .setFormat("json")
	.setConfig(new JsonObject()
		.put("key", "object_key")
		.put("bucket", "bucketName")
		.put("region", "us-west-2")
		.put("access_key", "my_aws_access_key")
		.put("secret_access_key", "my_aws_secret_access_key")
		.put("endpoint_url", "https://my-minio-cluster:9000"));
```