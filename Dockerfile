# Etapa 1: Compilación
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# --- AÑADE ESTA LÍNEA ---
RUN chmod +x gradlew
# ------------------------

RUN ./gradlew build -x test

# Etapa 2: Imagen de ejecución
FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080
COPY --from=build /home/gradle/src/applications/app-service/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

ARG POSTGRES_ADDON_HOST
ENV POSTGRES_ADDON_HOST ${POSTGRES_ADDON_HOST}
ARG POSTGRES_ADDON_USER
ENV POSTGRES_ADDON_USER ${POSTGRES_ADDON_USER}
ARG POSTGRES_ADDON_PASSWORD
ENV POSTGRES_ADDON_PASSWORD ${POSTGRES_ADDON_PASSWORD}
ARG POSTGRES_ADDON_DB
ENV POSTGRES_ADDON_DB ${POSTGRES_ADDON_DB}