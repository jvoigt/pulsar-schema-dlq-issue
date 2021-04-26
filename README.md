I notices an Issue with Apache Pulsar 2.7.1

When using the SchemaRegistry and the DeadLetterQueue the dead Messages will not by pusblished.

# How to use

just startup a pulsar-standalone https://pulsar.apache.org/docs/en/standalone-docker/

```
$ docker run -it \
  -p 6650:6650 \
  -p 8080:8080 \
  --mount source=pulsardata,target=/pulsar/data \
  --mount source=pulsarconf,target=/pulsar/conf \
  apachepulsar/pulsar:2.7.1 \
  bin/pulsar standalone
```

And run the project with mvn compile exec:java -Dexec.mainClass="com.jvoigt.issue.WorkingExample"

```
mvn compile exec:java -Dexec.mainClass="com.jvoigt.issue.BrokenJsonExample" > brokenjson.log 2>&1
```