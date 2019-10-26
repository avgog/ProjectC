#!/bin/bash
pm2 flush
pm2 restart 0
sleep 5 
curl -d '{ "query": "SELECT * FROM test;"}' -H "Content-Type:application/json" -X POST localhost:666/query
pm2 logs --raw
