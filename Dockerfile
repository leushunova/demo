FROM openjdk:11
#за основу взята 11 версия джавы

ARG JAR_FILE=target/demo-1.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]