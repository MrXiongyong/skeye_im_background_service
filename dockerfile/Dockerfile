FROM harbor.dcos.xixian.unicom.local/zongbu_tianyan_test/centos-java-invokechain:1.8
COPY confd/ /cbss/app/apps/confd/
COPY skeye_im_background_service-0.0.1-SNAPSHOT.jar  /home/microservices/
COPY startService.sh  /home/microservices/
RUN chmod -R 777  /home/microservices/
RUN mkdir /home/microservices/config/
WORKDIR /home/microservices/
EXPOSE 8082
