FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./wechat_banner-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENV PATH /usr/lib:$PATH
ENV REDIS_HOST r-c8dd9211071469c4.redis.rds.aliyuncs.com
ENV REDIS_PASSWORD=""
ENV REDIS_DATABASE 12
ENV MYSQL_HOST_AND_PORT 数据库连接地址
ENV MYSQL_USERNAME 用户名
ENV MYSQL_PASSWORD 密码
ENV RABBITMQ_HOST RABBITMQ地址
ENV RABBITMQ_PORT RABBITMQ端口
ENV RABBITMQ_USERNAME RABBITMQ用户名
ENV RABBITMQ_PASSWORD RABBITMQ密码
ENV RABBITMQ_VHOST /
ENV DEBUG false
ENV SHOW_SQL false
ENV LOGGING_LEVEL_SQL ERROR
ENV LOGGING_LEVEL_TYPE ERROR
ENV MAX_THREAD 10
ENV WECHAT_APP_ID 微信AppId
ENV WECHAT_APP_SECRET 微信AppSECRET
ENV INSURANCE_PINGAN_GET_TOKEN_URL https://api.pingan.com.cn/oauth/oauth2/access_token?client_id=P_QYKJ_TICKET&grant_type=client_credentials&client_secret=ur6X5z1H
ENV INVOICE_URL https://www.fapiao.com:63089/fpt-dsqz/invoice
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
