# Langsapp API layer

This directory contains the definitions of the APIs used in the project between applications.
The APIs are defined with
[interface description language](https://en.wikipedia.org/wiki/Interface_description_language)
to avoid enforcing any specific language for implementations. 
Additionally, API specifications serves as the source of documentation.

## Open API public specs

Available public APIs are specified in [public](public) directory following 
[Open API standard](https://www.openapis.org/).

### Validating specifications

Project contains Gradle tasks that allow to verify if the specifications are valid.

From top level directory execute following command

```shell
./gradlew api:validateOpenApiSpecs
```

### Building Documentation

Project provides set of Gradle tasks for preparing the interactive documentation page 
based on [Swagger UI](https://swagger.io/tools/swagger-ui/) .

Building docker image with documentation page

```shell
./gradlew api:rebuildDockerImage
```

Publishing image to the registry

```shell
./gradlew api:pushDockerImage
```
