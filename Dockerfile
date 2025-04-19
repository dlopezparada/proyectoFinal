
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar todo el proyecto
COPY . .

# Dar permisos al Maven Wrapper (solo si estás en Linux/macOS)
RUN chmod +x mvnw

# Construir el .jar sin correr tests
RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiar el .jar generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto que usa Spring Boot
EXPOSE 8082

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
