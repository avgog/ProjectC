#!/bin/bash

SENDGRID_API_KEY="<SENDGRID-API-KEY>"

######################################################
#                      Settings                      #
######################################################
logfile="/var/log/ovnotifier.log"                    #
#logfile="/dev/null"                                 #
saveRoutes="/routes"                                 #
apiPort=666                                          #
apiHost="localhost"                                  #
intermediateStops="true"                             #
from_email="<FROM@email>"                            #
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
cat="/bin/cat"                                       #
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
      -X GET ${apiHost}:${apiPort}/time | $jq '.[]'
}

getUser() { # Getting the time
  $curl -s -d "{ \"id\": \"${1}\"}" \
      -H "Content-Type:application/json"\
      -X GET ${apiHost}:${apiPort}/user | $jq '.[]'
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

  # Extracting start and end time from json
  endTime=$( getTime $( epoch $( $echo $route | $jq '.plan .itineraries | .[].endTime') ) )
  startTime=$( getTime $( epoch $( $echo $route | $jq '.plan .itineraries | .[].startTime') ) )

  log $( $printf "[ route: { from: $from & to:  $to } & appointment: $time & startTime: $startTime & arrival: $endTime & date: $date ]")

  # Save routes to file
  currentRoute=$( $echo $route | $jq '.plan .itineraries | .[] ' )
  $echo $currentRoute | $jq '.legs | .[] ' > ${saveRoutes}/${from}-${to}-${date}T${time}-full.route
  $echo $currentRoute | $jq '.legs | .[] | {from,to,mode,startTime,endTime,intermediateStops}' > ${saveRoutes}/${from}-${to}-${date}T${time}-int.route
  $echo $currentRoute | $jq '.legs | .[] | {from,to,mode,startTime,endTime}' > ${saveRoutes}/${from}-${to}-${date}T${time}.route

  # Update startTime in database
  updateTime $CURRENT_TIMESCHEME_ID $startTime

  ## Check if the time the route starts is still the same, if not get the route and resend email with scheme and time of department.
  if [[ "$startTime" != "$TIMESCHEME_LEAVE" ]]; then
    # NOTIFY USER THAT HE / SHE HAS TO LEAVE AN OTHER TIME THAN BEFORE
    PATH_TO_ROUTE="${saveRoutes}/${from}-${to}-${date}T${time}-full.route"

    FULL_ROUTE=$($cat $PATH_TO_ROUTE | $jq -s '.')
    ROUTE_LENGTH=$($echo $FULL_ROUTE | $jq '. | length'); ROUTE_LENGTH=$((${ROUTE_LENGTH} - 1))
    
    SCHEME=""
    for i in $($seq 0 $ROUTE_LENGTH); do
      LOCATION_FROM=$( $echo $FULL_ROUTE | $jq ".[$i].from.name" | $sed 's/"//g')
      LOCATION_TO=$( $echo $FULL_ROUTE | $jq ".[$i].to.name" | $sed 's/"//g')
      MODE=$( $echo $FULL_ROUTE | $jq ".[$i].mode" | $sed 's/"//g')
      STARTTIME=$(( $( $echo $FULL_ROUTE | $jq ".[$i].startTime" | $sed 's/"//g') - 3600000 ))
      ENDTIME=$(( $( $echo $FULL_ROUTE | $jq ".[$i].endTime" | $sed 's/"//g') - 3600000 ))
      if [[ "$LOCATION_TO" != "NONE" ]]; then
        if [[ "$LOCATION_FROM" != "NONE" ]]; then
          SCHEME="${SCHEME}\\n\\n$($echo "$($DATE -d @$( $echo $STARTTIME | $cut -c 1-10 ) +'%H:%M') - $($DATE -d @$( $echo $ENDTIME | cut -c 1-10 ) +'%H:%M') | $MODE | $LOCATION_FROM -> $LOCATION_TO")"
        else
          SCHEME="${SCHEME}\\n\\n$($echo "$($DATE -d @$( $echo $STARTTIME | $cut -c 1-10 ) +'%H:%M') - $($DATE -d @$( $echo $ENDTIME | cut -c 1-10 ) +'%H:%M') | $MODE | -> $LOCATION_TO")"
        fi
      fi
    done
    
    TEMPLATE='--- NEDERLANDS ---\n\nBeste,\n\nUw route begint op DATUM om TIJD. Onderaan deze mail vind u het schema van de route. Het is mogelijk dat deze nog verandert maar dan houden we u op de hoogte.\n\n\n--- English ---\n\nDear,\n\nYour route starts DATUM at TIJD. At the end of this email you will find the scheme of the route. It is possible that it will change over time but we will notify you if so.\n\n---\n\nSCHEME'

    DATA=$($echo $SCHEME | $sed 's/\\/\\\\/g' )
    MAIL=$($echo $TEMPLATE | $sed "s/SCHEME/$DATA/g; s/DATUM/$date/g; s/TIJD/$startTime/g")

    $curl --request POST \
            --url https://api.sendgrid.com/v3/mail/send \
            --header "Authorization: Bearer $SENDGRID_API_KEY" \
            --header 'Content-Type: application/json' \
            --data "{\"personalizations\": [{\"to\": [{\"email\": \"$email\"}]}],\"from\": {\"email\": \"$from_email\"},\"subject\": \"Test.\",\"content\": [{\"type\": \"text/plain\", \"value\": \"$MAIL\"}]}"


  fi
}

######################################################
#                  Run the program                   #
######################################################
## Getting information
CURRENT_TIMESCHEME_ID=$1
  CURRENT_TIMESCHEME=$( getTimeScheme $CURRENT_TIMESCHEME_ID )
  CURRENT_ROUTE=$(getRoute $( $echo $CURRENT_TIMESCHEME | $jq '.route_id') )
  LAST_NOTIFIED_EPOCH=$( $echo $CURRENT_TIMESCHEME | $jq '.notified' | sed 's/"//g' )
  TIMESCHEME_LEAVE=$( $echo $CURRENT_TIMESCHEME | $jq '.timeofstart' | sed 's/"//g' )
  LAST_CHECK=$( $echo $CURRENT_TIMESCHEME | $jq '.last_checked' | $sed 's/\"//g' )
  user_id=$($echo $CURRENT_ROUTE | $jq '.[0].user_id' | $sed -e 's/"//g')
  CURRENT_USER=$(getUser $user_id)
  email=$($echo $CURRENT_USER | $jq '.email' | $sed -e 's/"//g')
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

## Function that notifies user that he / she needs to leave in 10 minutes
Notify(){
  AWAY_EPOCH=$($echo $($DATE --date="$TODAY_DATE $startTime" +"%s" )) ## Epoch time format of the time of department
  NOTIFY_EPOCH=$( $echo $(($AWAY_EPOCH - 600)) ) ## Get the time you notified.

  ## Check if time you need to leave is in a 10 minute margin, if so, notify and update timestamp so it does not send twice.
  if [[ $CURRENT_EPOCH -lt $AWAY_EPOCH && $CURRENT_EPOCH -ge $NOTIFY_EPOCH ]]; then
    ## CHECK IF NOT ALREADY NOTIFIED
    if [[ $LAST_NOTIFIED_EPOCH -eq 'NULL' || $LAST_NOTIFIED_EPOCH -eq 'null' ]]
    then
      Route ## For getting the latest route information.
      PATH_TO_ROUTE="${saveRoutes}/${from}-${to}-${date}T${time}-full.route"
      FULL_ROUTE=$($cat $PATH_TO_ROUTE | $jq -s '.')
      ROUTE_LENGTH=$($echo $FULL_ROUTE | $jq '. | length'); ROUTE_LENGTH=$((${ROUTE_LENGTH} - 1))
      
      SCHEME=""
      for i in $($seq 0 $ROUTE_LENGTH); do
        LOCATION_FROM=$( $echo $FULL_ROUTE | $jq ".[$i].from.name" | $sed 's/"//g')
        LOCATION_TO=$( $echo $FULL_ROUTE | $jq ".[$i].to.name" | $sed 's/"//g')
        MODE=$( $echo $FULL_ROUTE | $jq ".[$i].mode" | $sed 's/"//g')
        STARTTIME=$(( $( $echo $FULL_ROUTE | $jq ".[$i].startTime" | $sed 's/"//g') - 3600000 ))
        ENDTIME=$(( $( $echo $FULL_ROUTE | $jq ".[$i].endTime" | $sed 's/"//g') - 3600000 ))
        if [[ "$LOCATION_TO" != "NONE" ]]; then
          if [[ "$LOCATION_FROM" != "NONE" ]]; then
            SCHEME="${SCHEME}\\n\\n$($echo "$($DATE -d @$( $echo $STARTTIME | $cut -c 1-10 ) +'%H:%M') - $($DATE -d @$( $echo $ENDTIME | cut -c 1-10 ) +'%H:%M') | $MODE | $LOCATION_FROM -> $LOCATION_TO")"
          else
            SCHEME="${SCHEME}\\n\\n$($echo "$($DATE -d @$( $echo $STARTTIME | $cut -c 1-10 ) +'%H:%M') - $($DATE -d @$( $echo $ENDTIME | cut -c 1-10 ) +'%H:%M') | $MODE | -> $LOCATION_TO")"
          fi
        fi
      done
      
      TEMPLATE='--- NEDERLANDS ---\n\nBeste,\n\nUw route begint binnen 10 minuten. Onderaan deze mail vind u het schema van de route.\n\n\n--- English ---\n\nDear,\n\nYour route starts in 10 minutes. At the end of this email you will find the scheme of the route.\n\n---\n\nSCHEME'
      DATA=$($echo $SCHEME | $sed 's/\\/\\\\/g' )
      MAIL=$($echo $TEMPLATE | $sed "s/SCHEME/$DATA/g")
      $curl --request POST \
              --url https://api.sendgrid.com/v3/mail/send \
              --header "Authorization: Bearer $SENDGRID_API_KEY" \
              --header 'Content-Type: application/json' \
              --data "{\"personalizations\": [{\"to\": [{\"email\": \"$email\"}]}],\"from\": {\"email\": \"$from_email\"},\"subject\": \"Test.\",\"content\": [{\"type\": \"text/plain\", \"value\": \"$MAIL\"}]}"

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

