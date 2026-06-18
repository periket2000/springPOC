# Spring POC

Multi-module Spring Boot 3.2.5 project with shared Azure ServiceBus support (receiver/sender) in `core`.

## Modules

| Module | Description | Port |
|--------|-------------|------|
| `core` | Shared library: ServiceBus consumer, sender, auto-configuration, handler interface | — |
| `bd-poc` | Spring Boot app with PostgreSQL | 8081 |
| `sb-receiver-poc` | ServiceBus message receiver (queues + topics/subscriptions) | 8082 |
| `sb-sender-poc` | Scheduled ServiceBus message sender (every 5s) | 8083 |

## Prerequisites

- Java 21
- Docker (for PostgreSQL and building images)

## Quick Start

```bash
# Build all modules
./gradlew build

# Run individual apps
./gradlew :bd-poc:bootRun
./gradlew :sb-receiver-poc:bootRun
./gradlew :sb-sender-poc:bootRun
```

## PostgreSQL

```bash
docker run --name postgres \
  -e POSTGRES_USER=tu_usuario \
  -e POSTGRES_PASSWORD=db_password \
  -e POSTGRES_DB=springPoc \
  -v ~/docker_volumes/postgres/pgdata:/var/lib/postgresql \
  -p 5432:5432 \
  -d postgres:latest
```

## Docker Images

```bash
# Build all service images (bd-poc, sb-receiver-poc, sb-sender-poc)
./gradlew buildImages

# Build individual image
docker build -f bd-poc/Dockerfile -t bd-poc:latest .
docker build -f sb-receiver-poc/Dockerfile -t sb-receiver-poc:latest .
docker build -f sb-sender-poc/Dockerfile -t sb-sender-poc:latest .
```

## ServiceBus Configuration

Auto-configuration activates when `azure.servicebus.connection-string` is set.

### Properties format

```properties
azure.servicebus.connection-string=${AZURE_SERVICEBUS_CONNECTION_STRING}

# Queues with simple map keys
azure.servicebus.queues.q1.name=${QUEUE1}
azure.servicebus.queues.q2.name=${QUEUE2}

# Topics with subscription
azure.servicebus.topics.t1.name=${TOPIC1}
azure.servicebus.topics.t1.subscription=${SUB1}
```

### Handler convention

Handlers are resolved by bean name convention `{key}Handler` (e.g. key `q1` → bean `q1Handler`). If no bean exists, `DefaultMessageHandler` prints the message to console.

```java
@Component("q1Handler")
public class PrintMessageHandler implements ServiceBusMessageHandler {
    @Override
    public void handleMessage(String message) {
        System.out.println(message);
    }
}
```

### Running with Docker

```bash
docker run -p 8082:8082 \
  -e AZURE_SERVICEBUS_CONNECTION_STRING="Endpoint=sb://..." \
  -e QUEUE1=my-queue \
  -e QUEUE2=my-other-queue \
  -e TOPIC1=my-topic \
  -e SUB1=my-subscription \
  sb-receiver-poc:latest
```

### Environment variable mapping

| Property | Env var |
|----------|---------|
| `azure.servicebus.connection-string` | `AZURE_SERVICEBUS_CONNECTION_STRING` |
| `azure.servicebus.queues.q1.name` | `QUEUE1` |
| `azure.servicebus.queues.q2.name` | `QUEUE2` |
| `azure.servicebus.topics.t1.name` | `TOPIC1` |
| `azure.servicebus.topics.t1.subscription` | `SUB1` |

## Sender

`sb-sender-poc` sends `{"hola":"mundo"}` to `QUEUE1` every 5 seconds via `ServiceBusSender`.

## Project Structure

```
springPOC/
├── build.gradle
├── settings.gradle
├── core/
│   └── src/main/java/es/mybi/demo/core/servicebus/
│       ├── ServiceBusProperties.java
│       ├── receiver/
│       │   ├── ServiceBusConsumer.java
│       │   ├── ServiceBusAutoConfiguration.java
│       │   ├── ServiceBusMessageHandler.java
│       │   └── DefaultMessageHandler.java
│       └── sender/
│           ├── ServiceBusSender.java
│           └── ServiceBusSenderAutoConfiguration.java
├── bd-poc/
│   ├── Dockerfile
│   └── src/
├── sb-receiver-poc/
│   ├── Dockerfile
│   └── src/
└── sb-sender-poc/
    ├── Dockerfile
    └── src/
```
