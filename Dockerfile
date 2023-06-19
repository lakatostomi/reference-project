FROM openjdk:17-jdk-alpine
ENV JDK_VERSION 17
RUN mkdir -p home/app
COPY target/ccapp.war home/app
EXPOSE 8080
CMD "java" "-jar" "home/app/ccapp.war"