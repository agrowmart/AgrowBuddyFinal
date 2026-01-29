# =========================
# BUILD STAGE
# =========================
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests

# =========================
# RUNTIME STAGE
# =========================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:MaxMetaspaceSize=256m \
-XX:+UseG1GC -XX:MaxGCPauseMillis=200 \
-XX:+UseContainerSupport -XX:+ExitOnOutOfMemoryError \
-Dspring.profiles.active=prod \
-Duser.timezone=Asia/Kolkata \
-Dfile.encoding=UTF-8"

EXPOSE 8080

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
