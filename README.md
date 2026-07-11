# Java REST API

A simple Spring Boot REST API with Prometheus metrics, request IDs, file logging, a multi-stage Docker build, and Docker Compose.

## Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/health` | Application health response |
| GET | `/metrics` | Prometheus-compatible metrics |
| GET | `/broker/{name}` | Returns request ID and broker name |
| GET | `/broker/{name}/{details}` | Returns name, supplied details, and randomly selected details |
| GET | `/dac/{number}` | Returns request ID, input number, and a generated random number |

The application also exposes Spring Actuator's Prometheus endpoint internally at `/actuator/prometheus`, while `/metrics` is mapped directly to the same Prometheus output.

## Run with Docker Compose

```bash
docker compose up --build
```

The API is available at `http://localhost:5050`.

## Examples

```bash
curl http://localhost:5050/health
curl http://localhost:5050/metrics
curl http://localhost:5050/broker/alice
curl http://localhost:5050/broker/alice/premium
curl http://localhost:5050/dac/100
```

You can provide your own request ID:

```bash
curl -H "X-Request-Id: demo-123" http://localhost:5050/broker/alice
```

## Tracing with Jaeger

The application now exports distributed traces to Jaeger over OTLP. When you start the stack with Docker Compose, a Jaeger container is also launched.

- Application UI: http://localhost:5050
- Jaeger UI: http://localhost:16686

If you run the app locally outside Docker, set the environment variable before starting it:

```bash
export OTLP_TRACING_ENDPOINT=http://localhost:4318/v1/traces
mvn spring-boot:run
```

## Logs

Logs are written to:

```text
./log/application.log
```

The Compose volume maps the host `./log` directory to `/app/log` in the container.

## Run locally

Requirements: Java 21 and Maven 3.9+.

```bash
mvn spring-boot:run
```
