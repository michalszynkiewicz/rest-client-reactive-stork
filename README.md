# Quarkus REST Client Reactive + SmallRye Stork demo project
This is a demo project used in the [Quarkus Insights #70](https://www.youtube.com/watch?v=l3mLKU3wR2A).

The service uses a REST Client to periodically call a remote service. 
The client is controlled with a single endpoint that can start and stop sending.

## Running

To start the service, build it and run the created jar:
```shell
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

This will start the application on http://localhost:8080.

## Usage
Go to http://localhost:8080 in your browser, the page has a single button that displays the status of the client.
Click on it to change the status.

## Examples
The commits in the `main` branch of the project corresponds to the demo steps on the insights.
The `kubernetes` branch, shows how to use the project on Kubernetes.
