# 基础镜像使用的java版本
#FROM java:8
FROM openjdk:11-jdk-slim
# 作者
MAINTAINER CharwayH
WORKDIR /app
# 将jar包添加到容器中并更名为 esb-support.jar(这个最重要！！！)
ADD target/i18n-example-1.0.0-SNAPSHOT.jar   i18n-example.jar
#COPY target/i18n-example-1.0.0-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-jar", "i18n-example.jar"]