FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY build/libs/futurebattlegrounds-*-all.jar futurebattlegrounds.jar
EXPOSE 8080
EXPOSE 50051
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar futurebattlegrounds.jar