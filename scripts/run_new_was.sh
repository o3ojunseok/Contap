#!/bin/bash

 CURRENT_PORT=$(cat /etc/nginx/conf.d/service-url.inc | grep -Po '[0-9]+' | tail -1)
 TARGET_PORT=0

 echo "> Current port of running WAS is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ -z ${TARGET_PID} ]
then
  echo "> 종료할 것 없음"
#  echo "> 종료할 것 없음."
else
  kill -15 ${TARGET_PID}
fi

#nohup java -jar -Dserver.port=${TARGET_PORT} /home/ubuntus/contap/build/libs/*SNAPSHOT.jar &
nohup java -jar -Dserver.port=${TARGET_PORT} /home/ubuntu/contap/build/libs/*SNAPSHOT.jar --spring.config.location=file:/home/ubuntu/contap/build/libs/application.properties &

echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0
