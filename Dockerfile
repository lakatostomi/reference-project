FROM maven:latest as build
ENV HOME=/home/tmp
RUN mkdir -p $HOME
WORKDIR $HOME

ADD . $HOME
RUN --mount=type=cache,target=/root/.m2 mvn -f $HOME/pom.xml clean package -DskipTests

FROM openjdk:17-jdk-alpine
RUN mkdir -p home/app
COPY --from=build /home/tmp/target/ccapp.war home/app/ccapp.war
EXPOSE 8080
ENTRYPOINT "java" "-jar" "/home/app/ccapp.war"