#!/bin/bash
pm2 flush
pm2 restart 0
sleep 2 
curl -d '{"key": "test", "user": "test_user", "pass": "pa$$w0rd", "data": {"fields": ["id"], "filter": {"test": "this is a test"} } }' -H "Content-Type:application/json" -X GET localhost:666
pm2 logs --raw
