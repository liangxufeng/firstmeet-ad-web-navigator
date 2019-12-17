FROM docker1.16801.com/common/alpine-java:8u191b12_server-jre_unlimited
MAINTAINER dongyuqni <dongyunqi@16801.com>
ENV TZ=Asia/Shanghai
#设置时区
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo '$TZ' > /etc/timezone
ADD /build/distributions/app.tar /
CMD /app/bin/startup
EXPOSE 8080