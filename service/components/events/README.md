# Langsapp Service events

This module holds events definitions in the system.

To keep things simple, the events are defined in pure kotlin's data classes.

Unfortunately I didn't find the way to represent these events in some interface definition language:
- [Protocol buffers](https://github.com/protocolbuffers/protobuf) was considered, but it didn't work since reflection is being used and it caused problems with GraalVM native images
- [Async API](https://www.asyncapi.com/) was considered, but its generator didn't support pure kotlin/java version and instead only suitable was `spring boot` that is too specific

There are other possible solutions that has not been verified yet (to be considered in the future):
* [Apache Avro](https://avro.apache.org/)
* [Open API schema spec](https://www.openapis.org/)
