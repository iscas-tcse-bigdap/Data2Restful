#! /bin/sh


active=test
if [ $spring_profiles_active ]
then
  active=$spring_profiles_active
fi
restart=0
pull=0
###提供两个option, -a 传递 sppring.profiles.active ; -r 是执行重启命令
while getopts ":a:rp" opt
do
    case $opt in
        a)
            echo "this is -a option. OPTARG=[$OPTARG] OPTIND=[$OPTIND], will set  spring.profiles.active=$OPTARG"
            active=$OPTARG
            ;;
            echo "this is -b option. OPTARG=[$OPTARG] OPTIND=[$OPTIND], git pull"
            pull=1
            ;;
        ?)
            echo "Usage: $(basename $0) [-a (set spring.profiles.active)] [-r (restart with not build) ] [-p (git pull)]"
            exit 1
            ;;
    esac
done

##部署位置
path=/opt/deploy_dc/K8sCli
jarName=k8scli-0.0.1-SNAPSHOT.jar
##源码位置
deployPath=/opt/deploy_dc/K8sCli

##启动服务
function start() {
  PID=$(ps -ef | grep $jarName | grep $active | grep -v grep | awk '{ print $2 }')
  if [ -z "$PID" ]
  then
      echo $jarName Application is already stopped
  else
    echo kill -9 $PID
    kill -9 $PID   ## -9 杀死
  fi
  sleep 1s ##等待1s

  cd $path
  echo "start $jarName with active $active"
  nohup java -jar $jarName --spring.profiles.active=$active  > out.log 2>&1 &
}

##启动
start