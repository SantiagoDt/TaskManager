# ---------- Stage 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /project

COPY pom.xml .


RUN mvn -q -B -DskipTests dependency:go-offline


COPY src ./src

RUN mvn -q -B package -DskipTests


# ---------- Stage 2: Runtime ----------

FROM eclipse-temurin:21-jre


WORKDIR /app

COPY --from=builder /project/target/*.jar /app/app.jar


ENV JAVA_OPTS=""

RUN useradd -r -u 10001 appuser && chown -R appuser /app
USER appuser


EXPOSE 8080


ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
