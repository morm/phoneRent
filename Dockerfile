FROM openjdk:11

COPY ./target/phoneRentManager.jar /home/phoneRentManager/
WORKDIR /home/phoneRentManager

CMD java -jar $PHONERENTMANAGER_OPTS /home/phoneRentManager/phoneRentManager.jar