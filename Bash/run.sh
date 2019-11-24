#!/bin/bash

bash="/bin/bash"

while true; do
  $bash -c "/root/ovnotifier.sh" &
  wait
  /bin/sleep 10
done
