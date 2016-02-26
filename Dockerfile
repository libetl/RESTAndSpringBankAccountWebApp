FROM maven:3-jdk-8-onbuild
MAINTAINER libetl
LABEL Description="bank account server" Vendor="libetl" Version="latest" Tag="libetl/bankaccountserver:latest"
EXPOSE 8080
CMD mvn jetty:run
