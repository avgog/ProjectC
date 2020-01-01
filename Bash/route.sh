#!/bin/bash

SENDGRID_API_KEY="<sendgris-api-key>"

######################################################
#                      Settings                      #
######################################################
logfile="/var/log/ovnofifier.log"                    #
#logfile="/dev/null"                                 #
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
  OUTPUT=$( $curl -s -d "{ \"time\": \"$CURRENT_EPOCH\", \"id\": \"$1\"}" \
      -H "Content-Type:application/json" \
      -X POST ${apiHost}:${apiPort}/checked )
  log $OUTPUT
}

updateNotified(){ # Update notified timestamp in the Times table
  OUTPUT=$( $curl -s -d "{ \"time\": \"$CURRENT_EPOCH\", \"id\": \"$1\"}" \
      -H "Content-Type:application/json" \
      -X POST ${apiHost}:${apiPort}/notified )
  log $OUTPUT
}

nullNotified(){ # Update notified timestamp in the Times table
  OUTPUT=$( $curl -s -d "{ \"id\": \"$1\"}" \
      -H "Content-Type:application/json" \
      -X POST ${apiHost}:${apiPort}/nullnotified )
  #log $OUTPUT
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

Route(){ # The program that runs and fetches the new time
  url="https://1313.nl/rrrr/plan?depart=false&"
  url="${url}from-latlng=${from}&to-latlng=${to}"
  url="${url}&date=${dateTime}&showIntermediateStops=${intermediateStops}"

  route=$($curl -s $url)

  endTime=$( getTime $( epoch $( $echo $route | $jq '.plan .itineraries | .[].endTime') ) )
  startTime=$( getTime $( epoch $( $echo $route | $jq '.plan .itineraries | .[].startTime') ) )

  log $( $printf "[ route: { from: $from & to:  $to } & appointment: $time & startTime: $startTime & arrival: $endTime & date: $date ]")

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
  LAST_NOTIFIED_EPOCH=$( $echo $CURRENT_TIMESCHEME | $jq '.notified' | sed 's/"//g' )
  LAST_CHECK=$( $echo $CURRENT_TIMESCHEME | $jq '.last_checked' | $sed 's/\"//g' )
  from=$(getPlace $($echo $CURRENT_ROUTE | $jq '.[0].start' | $sed -e 's/ /_/g') | $sed -e 's/"//g')
  to=$(getPlace $($echo $CURRENT_ROUTE | $jq '.[0].end' | $sed -e 's/ /_/g') | $sed -e 's/"//g')
  time=$( $echo $CURRENT_TIMESCHEME | $jq '.timeofarrival' | $sed -e 's/"//g')
  date=$( $echo $CURRENT_TIMESCHEME | $jq '.date' | $sed 's/\"//g' )
  dateTime="${date}T$( convertUTC2 $time )"
  CURRENT_EPOCH=$( $echo $($DATE +%s) )
  CHECK_IF=$($echo $(($LAST_CHECK + 3600)))
  TODAY=$( $DATE +%A )
  TODAY_DATE=$( $DATE '+%Y-%m-%d' )
  if [[ $CURRENT_EPOCH > $CHECK_IF || $LAST_CHECK -eq 'NULL' || $LAST_CHECK -eq 'null' ]]; then
    if [[ $date -eq $TODAY || $date -eq $TODAY_DATE ]]; then
      dateTime="${TODAY_DATE}T$( convertUTC2 $time )"
      date=${TODAY_DATE}
    fi
      Route
      log RENEWED TIMESCHEME $CURRENT_TIMESCHEME_ID
#  else
#    log does not renew
  fi


CURRENT_TIMESCHEME=$( getTimeScheme $CURRENT_TIMESCHEME_ID )
startTime=$( $echo $CURRENT_TIMESCHEME | $jq '.timeofstart' | $sed 's/"//g' )

Notify(){
  AWAY_EPOCH=$($echo $($DATE --date="$TODAY_DATE $startTime" +"%s" ))
  NOTIFY_EPOCH=$( $echo $(($AWAY_EPOCH - 600)) )

  if [[ $CURRENT_EPOCH -lt $AWAY_EPOCH && $CURRENT_EPOCH -ge $NOTIFY_EPOCH ]]; then
    ## CHECK IF NOT ALREADY NOTIFIED
    if [[ $LAST_NOTIFIED_EPOCH -eq 'NULL' || $LAST_NOTIFIED_EPOCH -eq 'null' ]]
    then
      $curl --request POST \
        --url https://api.sendgrid.com/v3/mail/send \
        --header "Authorization: Bearer $SENDGRID_API_KEY" \
        --header 'Content-Type: application/json' \
        --data '{"personalizations": [{"to": [{"email": "to@email.com"}]}],"from": {"email": "from@email.com"},"subject": "Route start binnen 10 minuten.","content": [{"type": "text/plain", "value": "Er begint een route binnen 10 minuten."}]}'

      log [ $CURRENT_EPOCH ] Notification send!

      ## Update notified timestamp with the epoch time when you need to leave.
      updateNotified $CURRENT_TIMESCHEME_ID $AWAY_EPOCH
    fi
  else
    nullNotified $CURRENT_TIMESCHEME_ID
  fi
}

if [[ $date -eq $TODAY || $date -eq $TODAY_DATE ]]; then
  Notify
fi

