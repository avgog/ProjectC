#!/bin/bash

##################################################################################
#                                    Settings                                    #
##################################################################################
logfile="/var/log/ovnofifier.log"                                                #
saveRoutes="/routes"                                                             #
apiPort=666                                                                      #
apiHost="localhost"                                                              #
intermediateStops="true"                                                         #
Routes="/root/route.sh"                                                          #
pid_file="/root/pid.file"                                                        #
max_amount_of_subprocesses="4"                                                   #
##################################################################################
#                                    Commands                                    #
##################################################################################
expr="/usr/bin/expr"                                                             #
echo="/bin/echo"                                                                 #
jq="/usr/bin/jq"                                                                 #
curl="/usr/bin/curl"                                                             #
seq="/usr/bin/seq"                                                               #
node="/usr/bin/node"                                                             #
awk="/usr/bin/awk"                                                               #
sed="/bin/sed"                                                                   #
cut="/usr/bin/cut"                                                               #
DATE="/bin/date"                                                                 #
printf="/usr/bin/printf"                                                         #
bash="/bin/bash"                                                                 #
wc="/usr/bin/wc"                                                                 #
cat="/bin/cat"                                                                   #
head="/usr/bin/head"                                                             #
touch="/usr/bin/touch"                                                           #
rm="/bin/rm"                                                                     #
##################################################################################
#                                   Functions                                    #
##################################################################################
                                                                                 #
getTimeScheme(){ # Getting all timeschemes                                       #
  TIMESCHEME=$($curl -s -X GET ${apiHost}:${apiPort}/times)                      #
}                                                                                #
                                                                                 #
log(){ # function to log                                                         #
   $echo $@ >> $logfile                                                          #
}                                                                                #
                                                                                 #
cleanup(){ # Cleanup for things we do not need anymore.                          #
  $echo "CLEANUP" > $logfile                                                     #
}                                                                                #
                                                                                 #
##################################################################################
#                                Run the program                                 #
##################################################################################
                                                                                 #
getTimeScheme                                                                    #
$touch $pid_file                                                                 #
for i in $($seq 0 `$expr $($echo $TIMESCHEME | $jq '. | length') - 1`); do       #
  ##Prepair                                                                      #
    CURRENT_TIMESCHEME_ID=$($echo $TIMESCHEME | $jq ".[$i].id")                  #
    pids=$( $cat $pid_file | $wc -l)                                             #
#   while [ $pids -gt 3 ]; do                                                    #
    while [ $pids -gt $max_amount_of_subprocesses ]; do                          #
      WAITING_PID=$( $cat $pid_file | $head -n 1 )                               #
      wait $WAITING_PID                                                          #
      $sed -i "/$WAITING_PID/d" $pid_file                                        #
      pids=$( $cat $pid_file | $wc -l)                                           #
    done                                                                         #
  ##Run                                                                          #
    $bash -c "$Routes $CURRENT_TIMESCHEME_ID"   &                                #
    $echo $! >> $pid_file                                                        #
done                                                                             #
                                                                                 #
for i in $( $cat $pid_file ); do                                                 #
  wait $i                                                                        #
done                                                                             #
$rm $pid_file                                                                    #
##################################################################################

