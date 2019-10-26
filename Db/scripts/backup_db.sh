#!/bin/bash
db='projectc'
echo "What is the name of the backup you want to create? " && read backup
mysqldump -u root $db > $backup
