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

# Fase 2: Multiproyecto

* Con el siguiente prompt:

```
Vale ahora quiero hacerlo multiproyecto, es decir, lo que hay en src sera bd-poc/src... 
y habrá un proyecto core que me permitira compartir codigo repetitivo.

Luego quiero otro proyecto que sea sb-poc que en la configuración llevará el azure-servicebus-connectionString, 
la cola o el topico y suscripcion y leera los mensajes imprimiendolos por pantalla. 

Ambos proyectos se tienen que poder levantar por separado. 

El codigo del servicebus tiene que estar en core, y será al importarlo en el proyecto que se le inyectaran las 
propiedades del proyecto, de forma que pueda usarse en cualquier proyecto simplemente cambiando las propiedades.
```

* Hemos pasado el poc a multiproyecto, con la siguiente estructura:

```
spring-poc/
├── build.gradle              # Raíz (solo configuración común)
├── settings.gradle            # Incluye core, bd-poc, sb-poc
├── core/                      # Librería compartida
│   ├── build.gradle
│   └── src/main/java/es/mybi/demo/core/servicebus/
│       ├── ServiceBusProperties.java       # @ConfigurationProperties
│       ├── ServiceBusConsumer.java          # Consumidor de Azure Service Bus
│       └── ServiceBusAutoConfiguration.java # Auto-configuración Spring Boot
├── bd-poc/                    # App Spring Boot con BD (tu código actual)
│   ├── build.gradle
│   ├── Dockerfile
│   └── src/                   # Código migrado con package es.mybi.demo.bdpoc.*
└── sb-poc/                    # Nueva app para leer de Service Bus
    ├── build.gradle
    ├── Dockerfile
    └── src/main/resources/application.properties  # Config Azure ServiceBus
```

## Cómo se usa:

|Acción|Comando|
|-------|--------|
|Build todo|./gradlew build|
|Build solo bd-poc|./gradlew :bd-poc:build|
|Build solo sb-poc|./gradlew :sb-poc:build|
|Ejecutar bd-poc|./gradlew :bd-poc:bootRun|
|Ejecutar sb-poc|./gradlew :sb-poc:bootRun|
|Docker bd-poc|docker build -f bd-poc/Dockerfile -t bd-poc:latest .|
|Docker sb-poc|docker build -f sb-poc/Dockerfile -t sb-poc:latest .|


