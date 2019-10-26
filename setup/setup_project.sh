#!/bin/bash

echo "What domain name do you use? " && read DOMAIN

git clone https://github.com/avgog/ProjectC && cd ProjectC

apt-get update
apt-get install mysql-server-5.7 haproxy git curl -y

/usr/bin/curl -sL https://deb.nodesource.com/setup_12.x | sudo -E bash -

apt-get install nodejs npm -y
PATH=$(pwd)

/bin/mkdir -p /opt/projectc/db_api/settings

/bin/cp -r ${PATH}/ProjectC/Api/db_api/* /opt/projectc/db_api/

cd /opt/projectc/db_api/
printf '
{
  "name": "project-c",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {
    "start": "npm run index.js && exit 0"
  },
  "author": "",
  "license": "ISC",
  "dependencies": {
    "express": "^4.17.1",
    "mysql": "^2.17.1"
  }
}
' > package.json

/bin/ln -s /usr/bin/nodejs /usr/bin/node
/usr/bin/npm install --save express
/usr/bin/npm install pm2@latest -g
/usr/bin/npm install --save mysql

/usr/local/bin/pm2 start index.js

/bin/cat ${PATH}/ProjectC/Db/dumps/projectc_prod.sql | /usr/bin/mysql -u root -p projectc

/bin/mv /etc/haproxy/haproxy.cfg /etc/haproxy/default_config.cfg

printf "
global
        log /dev/log    local0
        log /dev/log    local1 notice
        chroot /var/lib/haproxy
        stats socket /run/haproxy/admin.sock mode 660 level admin
        stats timeout 30s
        user haproxy
        group haproxy
        daemon

        ca-base /etc/ssl/certs
        crt-base /etc/ssl/private

	ssl-default-bind-ciphers ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:DHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA

        ssl-default-bind-options no-sslv3 no-tlsv10 no-tlsv11

        tune.ssl.default-dh-param 2048

defaults
        log     global
        mode    http
        option  httplog
        option  dontlognull
        timeout connect 5000
        timeout client  50000
        timeout server  50000
        errorfile 400 /etc/haproxy/errors/400.http
        errorfile 403 /etc/haproxy/errors/403.http
        errorfile 408 /etc/haproxy/errors/408.http
        errorfile 500 /etc/haproxy/errors/500.http
        errorfile 502 /etc/haproxy/errors/502.http
        errorfile 503 /etc/haproxy/errors/503.http
        errorfile 504 /etc/haproxy/errors/504.http

frontend frontend_http
        bind *:80

        http-request capture req.hdr(Host) len 25
        http-request capture req.hdr(User-Agent) len 100

	acl acl_projectc hdr(host) -i ${DOMAIN}

        use_backend backend_api if acl_projectc
        default_backend backend_default-deny

## API ##
backend backend_api
	server api 127.0.0.1:666

## DENY ALL
backend backend_default-deny
        http-request deny
" > /etc/haproxy/haproxy.cfg

/bin/systemctl reload haproxy.service

echo 'alias projectc="systemctl start haproxy.service && systemctl start mysql.service && pm2 start /opt/projectc/db_api/index.js"' >> ~/.bashrc && source ~/.bashrc


#just in case you use it as a test enviroment
echo "127.0.0.1   ${DOMAIN}" >> /etc/hosts
