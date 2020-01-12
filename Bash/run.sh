#!/bin/bash

## Keeps ovnofitier running

bash="/bin/bash"

while true; do
  $bash -c "/root/ovnotifier.sh" &
  wait
done
