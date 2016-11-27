FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/xa-07.jar /xa-07/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/xa-07/app.jar"]
