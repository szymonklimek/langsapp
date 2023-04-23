# Langsapp Service

This project is responsible for implementing server side of Langsapp APIs.

It consists of set of modules built and deployed together in single application 
located in [langsapp directory](langsapp).

## Requirements
* JDK 11
* Docker (including Compose)

## Build and run locally

### Infrastructure

Service requires Consul, which can be provided with docker compose by running following tasks:

```shell
./gradlew runLocalInfrastructure 
```

Once Consule container is ready, the necessary configuration can be uploaded via:

```shell
./gradlew initConfiguration
```

In case fresh infrastructure state is needed, current one can be cleaned by:

```shell
./gradlew cleanLocalInfrastructure
```

### Running service

Run gradle task

```shell
./gradlew langsapp:bootRun
```

## Building and deploying service

In order to reduce memory usage the service can be built as a native image to run on GraalVM.

To build the image simply run:

```shell
./gradlew bootBuildImage
```

Once the image is built, it can be uploaded to container registry.

In order to deploy the service, it is possible to use docker compose template located in 
[deployment directory](langsapp/deployment) of `langsapp` application module.
