FROM gradle:8.6-jdk21 AS builder
WORKDIR /app
COPY .. .

ENV GRADLE_OPTS="-XX:MaxMetaspaceSize=1g -Xmx2g -Dorg.gradle.jvmargs=-XX:MaxMetaspaceSize=1g -Xmx2g"

RUN gradle :ok-gas-station-be:build -x test

# Финальный образ
FROM eclipse-temurin:21-jre-jammy

# Копируем собранный артефакт из builder
COPY --from=builder /app/ok-gas-station-be/ok-gas-station-app-spring/build/libs/ok-gas-station-app-spring-0.0.1.jar /app/application.jar

# Указываем точку входа
ENTRYPOINT ["java", "-jar", "/app/application.jar"]

