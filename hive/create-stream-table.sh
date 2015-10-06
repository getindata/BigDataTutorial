#!/bin/bash

hive -hiveconf db=default -hiveconf location='/tmp/pig-etl' -f create-stream-table.hql
