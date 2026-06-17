# Spring POC (Java 11)

Basic concepts for Spring devel

- In memory test DB config.

# Postgres DB:

```
docker run --name postgres \
  -e POSTGRES_USER=tu_usuario \
  -e POSTGRES_PASSWORD=db_password \
  -e POSTGRES_DB=springPoc \
  -v ~/docker_volumes/postgres/pgdata:/var/lib/postgresql \
  -p 5432:5432 \
  -d postgres:latest
```

# Docker

```
# Construir
docker build -t spring-poc:latest .

# Ejecutar con defaults (necesita PostgreSQL en localhost:5432)
docker run -p 8081:8081 spring-poc:latest

# Ejecutar con variables de entorno (estilo Kubernetes)
docker run -p 8081:8081 \
  -e DB_URL=jdbc:postgresql://postgres-svc:5432/mydb \
  -e DB_USERNAME=user \
  -e DB_PASSWORD=pass \
  spring-poc:latest

# Ejecutar con acceso a localhost donde tengo postgres con docker escuchando
docker run -p 8081:8081 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/springPoc \
  spring-poc:latest

# Crear una red compartida entre docker y localhost (postgres)
docker network create mynet
docker network connect mynet postgres
docker run --network=mynet -p 8081:8081 \
  -e DB_URL=jdbc:postgresql://postgres:5432/springPoc \
  spring-poc:latest
```
