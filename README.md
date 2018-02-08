# toop-commons
Shared common TOOP components, codelists etc.


## toop-kafka-client

This is the client that is used to abstract the messaging to the central *Package Tracker* which is essentially an Apache Kafka server that is than queried by a UI from the playground. 


### Kafka Server Test

To run a test Kafka server on "localhost:9092" as expected by the default configuration, you can use Docker and run the following 2 machines (in that order)

```
docker run -d --name zookeeper -p 2181:2181 confluent/zookeeper
docker run -d --name kafka -p 9092:9092 --link zookeeper:zookeeper confluent/kafka
```

if you don't need them, you can stop them again with

```
docker stop kafka
docker stop zookeeper
```
