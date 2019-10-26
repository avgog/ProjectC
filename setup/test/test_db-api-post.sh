#!/bin/bash
pm2 flush
pm2 restart 0
sleep 2 
curl -d '{"key": "test", "user": "test_user", "pass": "pa$$w0rd", "data": {"column1": "test", "column2": "test2"} }' -H "Content-Type:application/json" -X POST localhost:666
pm2 logs --raw
