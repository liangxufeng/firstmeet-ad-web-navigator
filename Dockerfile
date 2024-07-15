FROM registry.cn-hangzhou.aliyuncs.com/xliangf/nginx:1.0-SNAPSHOT
MAINTAINER liangxufeng <1158539495@qq.com>
ENV TZ=Asia/Shanghai
#设置时区
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo '$TZ' > /etc/timezone
ADD gradlew clean
ADD gradlew clean classes --refresh-dependencies
ADD gradlew build
ADD /build/distributions/app.tar /
CMD /app/bin/startup
EXPOSE 8080