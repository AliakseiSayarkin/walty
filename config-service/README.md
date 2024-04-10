# Configuration Service

This project is a Configuration Server built using Java 21 and Spring Cloud. It serves as a central hub for managing
configuration properties across multiple microservices within a distributed system.

## Features

- **Centralized Configuration**: Provides a centralized location to manage configuration properties for all
  microservices.
- **Dynamic Configuration Refresh**: Supports dynamic configuration changes without the need for service restarts.
- **Integration with Spring Cloud**: Seamlessly integrates with Spring Cloud ecosystem for easy deployment and
  management.
- **Versioning and History**: Supports versioning and maintaining history of configuration changes for auditing and
  rollback purposes.
- **Secure Configuration Management**: Ensures secure management of sensitive configuration properties through
  encryption and access control.

## Requirements

- Java 21
- Spring Boot
- Spring Cloud
- Gradle (for building and dependency management)
- Configuration repository (e.g., Git) to store configuration files

## Usage

1. **Push Configuration Changes**: Push configuration changes to the configured repository. The Configuration Server
   will automatically detect and update the configurations for microservices.

2. **Access Configuration Properties**: Microservices can access their configuration properties by querying the
   Configuration Server using their application name.

   Example with Spring Boot:

    ```java
    @Value("${my.property}")
    private String myProperty;
    ```

3. **Dynamic Configuration Refresh**: To refresh configuration properties without restarting microservices, trigger a
   refresh endpoint in the microservice.

    ```bash
    POST /actuator/refresh
    ```

## Dependencies

* Java 21
* Spring Boot 3.2.2
* Gradle 8.5
* Groovy 5.0