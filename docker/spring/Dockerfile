FROM gradle:8.5-jdk17
WORKDIR /spring
COPY src ./src
COPY build.gradle .
COPY settings.gradle .
RUN gradle bootJar
CMD java -jar ./build/libs/course_mag-0.0.1-SNAPSHOT.jar
