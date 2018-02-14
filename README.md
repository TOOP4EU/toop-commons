# toop-commons
Shared common TOOP components, codelists etc.

# Development environment

* Java 1.8 latest
  * At least 1.8.0_91 is needed, so that access to "https://repo.maven.apache.org" works, because in this version the "DigitCert Global Root CA" support was added
* Eclipse 4.7 latest (JEE version - with m2e and egit contained)
* Apache Maven 3.x latest (only needed if building on the commandline is necessary)
* git Commandline client latest (only needed if building on the commandline is necessary)

Ensure the following environment variables are set (the values are just examples from my Windows machine)

```
JAVA_HOME=C:\Program Files\Java\jdk1.8.0_144
M2_HOME=C:\tools\apache-maven-3.5.0
```

`PATH` must contain `%JAVA_HOME%/bin` and `%M2_HOME%/bin`

If a proxy server is necessary, please make sure it is configured appropriately.

## Import of a project in Eclipse

* Start Eclipse
* Adopt the path to your Git repositories (Window | Predeferences | Team | Git) - must be done once only
* Choose the "git" perspective (right top)
* Paste the URL `https://github.com/TOOP4EU/toop-commons.git` in the window labeled `Git Repositories`.
  * Deselect the `Import projects` checkbox
* Switch to the `Java` perspective
* In the `Package Explorer` perform a right-mouse button click, choose `Import...`, open group `Maven`, select `Existing Maven Projects` and choose the local `toop-commons` folder as the base directory (the one including the hidden .git folder). 

# Contained projects

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
