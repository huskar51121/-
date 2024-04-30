# 使用 openjdk 作为基础镜像
FROM debian:latest

# 安装 Maven
RUN apt-get update && apt-get install -y apt-utils
RUN apt-get install -y maven

# 设置 Maven 环境变量
ENV MAVEN_HOME D:\apache-maven-3.6.3\apache-maven-3.6.3
ENV MAVEN_CONFIG C:\Users\HP\.m2\repository

# 将 Maven 可执行文件路径添加到 PATH
ENV PATH $MAVEN_HOME/bin:$PATH

# 设置工作目录
WORKDIR /app

# 将 Maven 项目的 pom.xml 文件复制到镜像中
COPY pom.xml /app

# 下载 Maven 依赖
RUN mvn dependency:go-offline

# 将整个项目复制到镜像中
COPY . /app

# 构建 Maven 项目
RUN mvn package

# 暴露 Spring Boot 应用程序的端口
EXPOSE 8080

# 定义容器启动命令
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
