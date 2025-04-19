
# Proyecto Sensor 

Este microservicio, desarrollado con Spring Boot, actúa como consumidor de Kafka, recibiendo mensajes desde un tópico remoto. Su función principal es procesar los mensajes recibidos y almacenarlos en una base de datos PostgreSQL.
---

## Descripción del Proyecto

Este microservicio forma parte de un sistema IoT, en el cual múltiples sensores publican datos hacia un broker Kafka. Su propósito es consumir, validar y almacenar dicha información de manera eficiente. Las principales características del servicio son:

- Se conecta al broker Kafka remoto `186.64.120.248:9092`
- Escucha el topic `tf-minera-01`
- Procesa mensajes en formato JSON, mapeados como objetos `SensorDataDto`
- Valida si el sensor existe en base a su `apiKey`
- Persiste los datos en la base de datos PostgreSQL denominada `proyecto`
- Está dockerizado para facilitar su despliegue e integración en distintos entornos

---

## Estructura del Proyecto

```plaintext
├── proyectoFinal/
│   docker-compose.yml
│   Dockerfile
│   init.sql
│   mvnw
│   mvnw.cmd
│   pom.xml
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   ├── com/
│   │   │   │   │   ├── futuro/
│   │   │   │   │   │   ├── proyecto/
│   │   │   │   │   │   │   AppConfig.java
│   │   │   │   │   │   │   ProyectoFinalApplication.java
│   │   │   │   │   │   │   ├── config/
│   │   │   │   │   │   │   │   KafkaConsumerConfig.java
│   │   │   │   │   │   │   │   SecurityConfig.java
│   │   │   │   │   │   │   ├── controllers/
│   │   │   │   │   │   │   │   AdminController.java
│   │   │   │   │   │   │   │   CompanyController.java
│   │   │   │   │   │   │   │   LocationController.java
│   │   │   │   │   │   │   │   SensorController.java
│   │   │   │   │   │   │   │   SensorDataController.java
│   │   │   │   │   │   │   ├── dto/
│   │   │   │   │   │   │   │   AdminDto.java
│   │   │   │   │   │   │   │   ApiResponse.java
│   │   │   │   │   │   │   │   CompanyDto.java
│   │   │   │   │   │   │   │   JsonDataDto.java
│   │   │   │   │   │   │   │   LocationDto.java
│   │   │   │   │   │   │   │   RequestDataDto.java
│   │   │   │   │   │   │   │   ResponseDataDto.java
│   │   │   │   │   │   │   │   SensorDataDto.java
│   │   │   │   │   │   │   │   SensorDataInputDto.java
│   │   │   │   │   │   │   │   SensorDataValueDto.java
│   │   │   │   │   │   │   │   SensorDataValueInputDto.java
│   │   │   │   │   │   │   │   SensorDto.java
│   │   │   │   │   │   │   │   SensorVariableDto.java
│   │   │   │   │   │   │   ├── exception/
│   │   │   │   │   │   │   │   GlobalExceptions.java
│   │   │   │   │   │   │   ├── listener/
│   │   │   │   │   │   │   │   SensorDataListener.java
│   │   │   │   │   │   │   ├── models/
│   │   │   │   │   │   │   │   Admin.java
│   │   │   │   │   │   │   │   Company.java
│   │   │   │   │   │   │   │   Location.java
│   │   │   │   │   │   │   │   Permiso.java
│   │   │   │   │   │   │   │   Role.java
│   │   │   │   │   │   │   │   RoleEnum.java
│   │   │   │   │   │   │   │   Sensor.java
│   │   │   │   │   │   │   │   SensorData.java
│   │   │   │   │   │   │   │   SensorDataValue.java
│   │   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   │   AdminRepository.java
│   │   │   │   │   │   │   │   CompanyRepository.java
│   │   │   │   │   │   │   │   LocationRepository.java
│   │   │   │   │   │   │   │   SensorDataRepository.java
│   │   │   │   │   │   │   │   SensorDataValuesRepository.java
│   │   │   │   │   │   │   │   SensorRepository.java
│   │   │   │   │   │   │   ├── services/
│   │   │   │   │   │   │   │   AdminService.java
│   │   │   │   │   │   │   │   AdminServiceImp.java
│   │   │   │   │   │   │   │   CompanyService.java
│   │   │   │   │   │   │   │   CompanyServiceImp.java
│   │   │   │   │   │   │   │   LocationService.java
│   │   │   │   │   │   │   │   LocationServiceImp.java
│   │   │   │   │   │   │   │   SensorDataService.java
│   │   │   │   │   │   │   │   SensorDataServiceImp.java
│   │   │   │   │   │   │   │   SensorDataValueService.java
│   │   │   │   │   │   │   │   SensorDataValueServiceImp.java
│   │   │   │   │   │   │   │   SensorService.java
│   │   │   │   │   │   │   │   SensorServiceImp.java
│   │   │   │   │   │   │   │   UserDetailsServiceImp.java
│   │   │   │   │   │   │   ├── utils/
│   │   │   │   │   │   │   │   Utility.java
│   │   │   ├── resources/
│   │   │   │   application.properties
│   │   │   │   database.properties

```

---

##  Tecnologías Utilizadas
El proyecto está construido utilizando el siguiente stack tecnológico:

- Java 21 (Eclipse Temurin) – Lenguaje principal del desarrollo
- Spring Boot (Web, Kafka, Security, Data JPA)
- Apache Kafka (Servicio de mensajería distribuido (broker remoto)
- PostgreSQL - Base de datos relacional, ejecutándose en un contenedor Docker
- Docker & Docker Compose – Para contenerización y orquestación del entorno
- Lombok - Para simplificar la escritura de código boilerplate
- Maven - Herramienta de gestión de dependencias y construcción del proyecto

---

## Cómo Ejecutar el Proyecto

### Requisitos:

- Docker instalado
- Puerto 8082 y 5433 libres (o modificarlos)
- Acceso a internet para conectarse al broker Kafka externo

---

### 1. Clonar el proyecto

```bash
git clone https://github.com/dlopezparada/proyectoFinal.git
cd proyectoFinal
```

---

### 2. Levantar con Docker

```bash
docker-compose up --build
```

Esto:

- Compila y construye la app
- Inicia la base de datos PostgreSQL en `localhost:5433`
- Levanta la app Spring Boot en `http://localhost:8082`
- Conecta automáticamente con Kafka externo

---

## Configuraciones importantes

### `application.properties`

```properties
server.port=8082

# Kafka
spring.kafka.bootstrap-servers=186.64.120.248:9092
spring.kafka.consumer.group-id=sensor-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*

# PostgreSQL
spring.datasource.url=jdbc:postgresql://postgres:5432/proyecto
spring.datasource.username=postgres
spring.datasource.password=123456
```

---

## Verificación y pruebas

### 1. Ver logs Kafka

Una vez que Kafka empiece a enviar mensajes, deberías ver en consola:

```
partitions assigned: [tf-minera-01-0]
Resetting offset for partition tf-minera-01-0 to position FetchPosition{offset=...}
No se encontró sensor para la API Key: ...
```

### 2. Ver base de datos

Entrá al contenedor PostgreSQL:

```bash
docker exec -it postgres bash
psql -U postgres -d proyecto
```

Y consultá los datos:

```sql
SELECT * FROM sensor;
```

---

##  Consideraciones

- Si el puerto `8082` está ocupado, podés modificarlo en:
  - `docker-compose.yml` → `ports`
  - `application.properties` → `server.port`
- El script `init.sql` se ejecuta al levantar la base si no existe la tabla

