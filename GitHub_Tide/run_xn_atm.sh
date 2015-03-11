#!/bin/sh

LANG=zh_CN
export LANG

pross=main.run_xn_atm
date=`ps -ef |grep $pross|grep -v "grep" |awk '{print $2}'`
echo now is $date

if [ -z $date ]
then
echo '$pross is NULL PRO'
else
kill -9 $date
echo kill -9 $date    $pross    OK
fi

cd /home/ljg/test
#JAVA_HOME=/home/ljg/rejdk/jdk1.6.0_23
#JAVA_VM=-hotspot

LIBRARY="."
for i in ./lib/*.jar; do
LIBRARY="$LIBRARY":"$i"
done

PRO=./GitHub_Tide.jar
CLASSPATH=$JAVA_HOME\lib\tools.jar:$JAVA_HOME\lib\dt.jar:$LIBRARY:$PRO
MEM_ARGS="-Xms128m -Xmx256m"
java $JAVA_VM $MEM_ARGS -cp $CLASSPATH main.run_xn_atm