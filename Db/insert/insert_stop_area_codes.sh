#!/bin/bash

DB_USER="root"
DB="projectc"
curl="/usr/bin/curl"
jq="/usr/bin/jq"
sed="/bin/sed"
echo="/bin/echo"
cat="/bin/cat"

INSERT_IT="/root/insert/exec.sh"
JSON_FILE="/root/insert/json.file"

$curl http://v0.ovapi.nl/stopareacode | $jq . > $JSON_FILE
ALL_STOPCODES=$($cat $JSON_FILE | $jq '. | keys' | $jq '.[]' | $sed 's/ /\n/g' | $sed 's/"//g')

for i in `$echo $ALL_STOPCODES`; do /bin/bash -c "$INSERT_IT $i" \& ; done

$mysql -u $DB_USER -D $DB -e "DELETE FROM Stops WHERE town=\"null\" or name=\"null\";"
$mysql -u $DB_USER -D $DB -e "DELETE FROM Stops WHERE town=\"'\" or name=\"'\";"
$mysql -u $DB_USER -D $DB -e "DELETE FROM Stops WHERE town=\"\" or name=\"\";"
$mysql -u $DB_USER -D $DB -e "DELETE FROM Stops WHERE longitude=\"3.3135424\" and latitude=\"47.974766\";"
$mysql -u $DB_USER -D $DB -e "DELETE FROM Stops WHERE town LIKE '-%' or name LIKE '-%';"
$mysql -u $DB_USER -D $DB -e "DELETE FROM Stops WHERE town LIKE '/%' or name LIKE '/%';"
$mysql -u $DB_USER -D $DB -e "DELETE FROM Stops WHERE town LIKE 'Onbekend' or name LIKE 'Onbekend';"
