# Subsquid indexers status API

A Spring Boot application which uses Quartz scheduler to periodically fetch the statuses of the running Subsquid indexers. The list of indexers is taken from Github. 

A REST API `GET /statuses` endpoint is provided to consume this information by [bots](https://github.com/singulart/subsquid-telegram-bot) and front-end dashboards.

For simplicity, all data retrieved by Quartz job is stored in memory. 

# Configuration  

The following environment variables affect the application:
1. `INDEXERS_LIST` - Direct URL to file on GitHub with the list of deployed indexers URLs
2. `REPEAT_INTERVAL_SECS` - time interval (in seconds) for Quartz to re-query all the statuses
3. `CONNECT_TIMEOUT_MILLIS` - Spring Rest Template connect timeout (in ms). 
4. `READ_TIMEOUT_MILLIS` - Spring Rest Template read timeout (in ms). See [SO comment](https://stackoverflow.com/a/3069450) for context.

# Building / Running Locally  

`mvn clean package`

`mvn spring-boot:run`

# Running in Prod
`java -jar indexer-api-0.0.1-SNAPSHOT.jar`

# Running as Docker container
To publish the image to docker registry 

`mvn compile jib:build`

To run the image

`docker run -d -p 8080:8080 --name indexer-api isonar/subsquid-indexers-api`

# Accessing the endpoint

`curl http://localhost:8080/statuses`
