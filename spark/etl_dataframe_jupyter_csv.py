from pyspark.sql import HiveContext
from pyspark import SparkConf, SparkContext
from pyspark.sql.functions import udf
from pyspark.sql.types import BooleanType, StringType, IntegerType
import sys

# Define UDFs
def isDurationCorrect(duration):
    return duration is not None and duration != 'INF' and 30 <= int(duration) and int(duration) <= 1200

def removeBrackets(text):
    return text[1:-1]

# Register UDFs
udfIsDurationCorrect=udf(isDurationCorrect, BooleanType())
udfRemoveBrackets=udf(removeBrackets, StringType())

# Define global variables
database = "<your-database>"

# Read in the dataset
logs = spark.read.***TODO***("/incoming/logs/upload", sep="\t", inferSchema=True, header="True")

# Process the dataset
# Leave only SongPlayed events
streams_raw = logs.***TODO***(logs['eventType'] == 'SongPlayed')

# Remove unnecessary 'eventType' column from dataset
streams_projected = streams_raw.***TODO***('eventType')

# Rename 'itemId' column into 'trackId'
streams = streams_projected.withColumnRenamed('itemId', 'trackId')

# Exclude records with incorrect value in 'duration' field 
streams_correct = streams.***TODO***(udfIsDurationCorrect('duration'))

# Remove brackets around 'ts' field and cast 'duration' field to integer type
streams_cleaned = streams_correct.***TODO***(udfRemoveBrackets('ts').alias('ts'), 'host', 'userId', 'trackId', streams_correct['duration'].cast('int'))

# Remove duplicates from dataset
streams_unique = streams_cleaned.***TODO***()

# Store dataset in Hive
streams_unique.write.***TODO***("%s.stream" % database, mode="overwrite")

# Dump few records on the console as sanity check
streams_unique.take(5)
