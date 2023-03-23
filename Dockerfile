FROM openjdk:8-jdk

MAINTAINER Faizul Haque Nayan "faizul.nayan@gmail.com"

WORKDIR /usr/local/bin/

COPY target/file-processing-0.0.1-SNAPSHOT.jar file-processing-0.0.1-SNAPSHOT.jar

EXPOSE 9008

CMD ["java", "-jar", "file-processing-0.0.1-SNAPSHOT.jar"]
