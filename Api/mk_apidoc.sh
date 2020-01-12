#!/bin/bash
/usr/bin/apidoc -i /opt/projectc/ -o /var/www/html/docs/ -e /opt/projectc/node_modules/
/bin/sed -i 's/<div id="generator"><\/div>//g' /var/www/html/docs/index.html
