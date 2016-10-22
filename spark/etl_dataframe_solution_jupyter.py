from pyspark.sql import HiveContext
from pyspark import SparkConf, SparkContext
from pyspark.sql.functions import udf
from pyspark.sql.types import BooleanType, StringType, IntegerType
import sys

# Define UDFs
def isDurationCorrect(duration):
    return duration is not None and duration != 'INF' and 30 <= int(duration) and int(duration) <= 1200

def removeBraces(text):
    return text[1:-1]

# Register UDFs
udfIsDurationCorrect=udf(isDurationCorrect, BooleanType())
udfRemoveBraces=udf(removeBraces, StringType())

# Define global variables
database = "tiger"

# Read in the dataset
logs = sqlContext.sql("SELECT * FROM %s.logs" % database)

# Process the dataset
streams_raw = logs.filter(logs['eventType'] == 'SongPlayed')
streams_projected = streams_raw.drop('eventType')
streams = streams_projected.withColumnRenamed('itemId', 'trackId')
streams_correct = streams.filter(udfIsDurationCorrect('duration'))
streams_cleaned = streams_correct.select(udfRemoveBraces('ts').alias('ts'), 'host', 'userId', 'trackId', streams_correct['duration'].cast('int'))
streams_unique = streams_cleaned.distinct()

# Store dataset
streams_unique.write.saveAsTable("%s.stream" % database, mode="overwrite")
streams_unique.take(5)
