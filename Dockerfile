FROM docker1.16801.com/common/alpine-java:8u191b12_server-jre_unlimited
ADD /build/distributions/app.tar /
WORKDIR /app
CMD ./bin/startup
EXPOSE 8080
EXPOSE 6565