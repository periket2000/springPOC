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
