FROM amazoncorretto:11-alpine
#COPY ./build/libs/owasp-0.0.2-SNAPSHOT.jar owasp.jar
#COPY ./build/libs/owasp-0.0.2-SNAPSHOT.jar owasp.jar
ENTRYPOINT ["java","-jar","/owasp.jar"]
EXPOSE 8090

