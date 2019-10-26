#!/bin/bash
systemctl start haproxy.service
systemctl start mariadb.service
pm2 start /nodejs/project-c/index.js
