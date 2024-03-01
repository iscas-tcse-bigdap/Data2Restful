
#! /bin/sh

##部署位置
path=/opt/deploy_dc/userAuth
jarName=UserAuth-0.0.1-SNAPSHOT.jar

##启动服务
function start() {
  PID=$(ps -ef | grep $jarName | grep -v grep | awk '{ print $2 }')
  if [ -z "$PID" ]
  then
      echo $jarName Application is already stopped
  else
    echo kill -9 $PID
    kill -9 $PID   ## -9 杀死
  fi
  sleep 1s ##等待1s

  cd $path
  echo "start $jarName"
  nohup java -jar $jarName > out.log 2>&1 &
}



##启动
start
