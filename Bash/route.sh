#!/bin/bash


######################################################
#                      Settings                      #
######################################################
logfile="/var/log/ovnofifier.log"                    #
saveRoutes="/routes"                                 #
apiPort=666                                          #
apiHost="localhost"                                  #
intermediateStops="true"                             #
######################################################
#                      Commands                      #
######################################################
echo="/bin/echo"                                     #
jq="/usr/bin/jq"                                     #
curl="/usr/bin/curl"                                 #
seq="/usr/bin/seq"                                   #
node="/usr/bin/node"                                 #
awk="/usr/bin/awk"                                   #
sed="/bin/sed"                                       #
cut="/usr/bin/cut"                                   #
DATE="/bin/date"                                     #
printf="/usr/bin/printf"                             #
######################################################
#                     Functions                      #
######################################################

#convertUTC2(){ # Converts time to the right timezone (UTC+2)
#  ## The time needs to be in UTC+2, in the Netherlands it is UTC+1 right now
#  $DATE -d "$1 +1 hour" +'%H:%M:%S'
#}
#
#getTime(){ #extract the time
#  $echo "$1" | $sed -e 's/T/ /g' | $awk '{print $2;}' | $sed -e 's/:/ /2' | $awk '{print $1;}'
#}
#
#getTimeScheme() { # Getting the time
#  $curl -s -d "{ \"id\": \"${1}\"}" \
#      -H "Content-Type:application/json"\
#      -X GET ${apiHost}:${apiPort}/time | jq '.[]'
#}
#
#getRoute() { # Getting the routes
#  $curl -s -d "{ \"route\": \"${1}\"}" -H "Content-Type:application/json" -X GET ${apiHost}:${apiPort}/routes
#}
#
#getPlace(){ # Getting the coordinates of a place (x,y)
#  STOP=$( $echo $1 | $sed 's/,/ /g' | $awk '{print $1;}' | $sed 's/_/ /g' | $sed 's/"//g')
#  TOWN=$( $echo $1 | $sed 's/,/ /g' | $awk '{print $2;}' | $sed 's/_/ /g' | $sed 's/"//g' | $cut -c 2-)
#  PLACE=$($curl -s -X GET -d "{\"town\":\"$TOWN\",\"stop\":\"$STOP\"}" -H "Content-Type:application/json" ${apiHost}:${apiPort}/stops)
#  long=$($echo $PLACE | $jq '.[0].longitude'); lat=$($echo $PLACE | $jq '.[0].latitude')
#  $echo "$lat,$long"
#}

epoch(){ # Converts epoch time to normal time
  $echo "new Date(${1})" | $node -p
}

log(){ # function to log
   $echo $@ >> $logfile
}

updateTime(){ # Update start in the Times table
  OUTPUT=$( $curl -s -d "{ \"time\": \"$2\", \"id\": \"$1\"}" \
      -H "Content-Type:application/json" \
      -X POST ${apiHost}:${apiPort}/starttime )
  log $OUTPUT
}

convertUTC2(){ # Converts time to the right timezone (UTC+2)
  ## in the Netherlands it is UTC+1 right now so +1 hour
  $DATE -d "$1 +1 hour" +'%H:%M:%S'
}

getTime(){ #extract the time
  $echo "$1" | $sed -e 's/T/ /g' | \
      $awk '{print $2;}' | $sed -e 's/:/ /2' \
      | $awk '{print $1;}'
}

getTimeScheme() { # Getting the time
  $curl -s -d "{ \"id\": \"${1}\"}" \
      -H "Content-Type:application/json"\
      -X GET ${apiHost}:${apiPort}/time | jq '.[]'
}

getRoute() { # Getting the routes
  $curl -s -d "{ \"route\": \"${1}\"}" \
      -H "Content-Type:application/json"\
      -X GET ${apiHost}:${apiPort}/routes
}

getPlace(){ # Getting the coordinates of a place (x,y)
  STOP=$( $echo $1 | $sed 's/,/ /g' \
      | $awk '{print $1;}' | $sed 's/_/ /g' \
      | $sed 's/"//g')
  TOWN=$( $echo $1 | $sed 's/,/ /g' | \
      $awk '{print $2;}' | $sed 's/_/ /g' | \
      $sed 's/"//g' | $cut -c 2-)
  PLACE=$($curl -s -X GET \
      -d "{\"town\":\"$TOWN\",\"stop\":\"$STOP\"}" \
      -H "Content-Type:application/json" ${apiHost}:${apiPort}/stops)
  long=$($echo $PLACE | $jq '.[0].longitude'); 
  lat=$($echo $PLACE | $jq '.[0].latitude')
  $echo "$lat,$long"
}

Route(){ # The program that runs...
  url="https://1313.nl/rrrr/plan?depart=false&"
  url="${url}from-latlng=${from}&to-latlng=${to}"
  url="${url}&date=${dateTime}&showIntermediateStops=${intermediateStops}"

  route=$($curl -s $url)

  endTime=$( getTime $( epoch $( $echo $route | $jq '.plan .itineraries | .[].endTime') ) )
  startTime=$( getTime $( epoch $( $echo $route | $jq '.plan .itineraries | .[].startTime') ) )

  log $( $printf "[ route: { from: $from & to:  $to } & appointment: $time & startTime: $startTime & arrival: $endTime ]")

  currentRoute=$( $echo $route | $jq '.plan .itineraries | .[] ' )
  $echo $currentRoute | $jq '.legs | .[] ' > ${saveRoutes}/${from}-${to}-${date}T${time}-full.route
  $echo $currentRoute | $jq '.legs | .[] | {from,to,mode,startTime,endTime,intermediateStops}' > ${saveRoutes}/${from}-${to}-${date}T${time}-int.route
  $echo $currentRoute | $jq '.legs | .[] | {from,to,mode,startTime,endTime}' > ${saveRoutes}/${from}-${to}-${date}T${time}.route

  updateTime $CURRENT_TIMESCHEME_ID $startTime
}


######################################################
#                  Run the program                   #
######################################################
CURRENT_TIMESCHEME_ID=$1
  CURRENT_TIMESCHEME=$( getTimeScheme $CURRENT_TIMESCHEME_ID )
  CURRENT_ROUTE=$(getRoute $( $echo $CURRENT_TIMESCHEME | $jq '.route_id') )
  from=$(getPlace $($echo $CURRENT_ROUTE | $jq '.[0].start' | $sed -e 's/ /_/g') | $sed -e 's/"//g')
  to=$(getPlace $($echo $CURRENT_ROUTE | $jq '.[0].end' | $sed -e 's/ /_/g') | $sed -e 's/"//g')
  time=$( $echo $CURRENT_TIMESCHEME | $jq '.timeofarrival' | $sed -e 's/"//g')
  date='2019-11-10'
  dateTime="${date}T$( convertUTC2 $time )"
  Route
  #/bin/sleep 5
  #log I DID IT
