FROM maven:3.2-jdk-7-onbuild
MAINTAINER libetl
LABEL Description="bank account server" Vendor="libetl" Version="latest" Tag="libetl/bankaccountserver:latest"
CMD mvn jetty:run