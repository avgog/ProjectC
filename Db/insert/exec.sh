#!/bin/bash

DB_USER="root"
DB="projectc"

echo="/bin/echo"
jq="/usr/bin/jq"
sed="/bin/sed"
mysql="/usr/bin/mysql"
cat="/bin/cat"
cut="/usr/bin/cut"

JSON_FILE="/root/insert/json.file"

RECORD=$($cat $JSON_FILE | $jq ".\"$1\"")
LATITUDE=$($echo $RECORD | $jq '.Latitude' | $sed 's/"//g')
LONGITUDE=$($echo $RECORD | $jq '.Longitude' | $sed 's/"//g')
STOPAREACODE=$($echo $RECORD | $jq '.StopAreaCode' | $sed 's/"//g')
TOWN=$($echo $RECORD | $jq '.TimingPointTown' | $sed 's/"//g')
halte=$($echo $RECORD | $jq '.TimingPointName' | $sed "s/$TOWN//g" | $sed 's/,//g' | $sed 's/"//g')
if [[ $halte == " "* ]]; then HALTE=$($echo $halte | $cut -c 1-); else HALTE=$($echo $halte); fi;
$mysql -u $DB_USER -D $DB -e "INSERT INTO Stops (town,name,stopareacode,longitude,latitude) VALUES (\"$TOWN\",\"$HALTE\",\"$STOPAREACODE\",\"$LONGITUDE\",\"$LATITUDE\");" 
