USE ${hiveconf:db};

DROP TABLE IF EXISTS stream;

CREATE EXTERNAL TABLE IF NOT EXISTS stream (
	server		STRING,
	timestamp	STRING,
	userid		INT,
	songid		INT,
	duration	INT
)
STORED AS TEXTFILE
LOCATION '${hiveconf:location}';
