from pyspark.sql import HiveContext
from pyspark import SparkConf, SparkContext
from pyspark.sql.functions import udf
from pyspark.sql.types import BooleanType, StringType, IntegerType
import sys


def isDurationCorrect(duration):
    return duration is not None and duration != 'INF' and 30 <= int(duration) and int(duration) <= 1200


def removeBraces(text):
    return text[1:-1]


if __name__ == "__main__":

    # Ensure that an input and output database is specified as a parameter
    if len(sys.argv) < 1:
        print('Usage: ' + sys.argv[0] + ' <database>')
        sys.exit(1)

    # Grab the parameters
    database = sys.argv[1]

    # Create a context for the job. The context is used to manage the job at a high level.
    appName = "ETL-%s" % database
    sc = SparkContext(appName = appName)
    sqlContext = HiveContext(sc)

    # Register UDFs
    udfIsDurationCorrect=udf(isDurationCorrect, BooleanType())
    udfRemoveBraces=udf(removeBraces, StringType())

    # Read in the dataset
    logs = sqlContext.***TODO***("SELECT * FROM %s.logs" % database)

    # Process the dataset
    streams_raw = logs.***TODO***(logs['eventType'] == 'SongPlayed')
    streams_projected = streams_raw.***TODO***('eventType')
    streams = streams_projected.***TODO***('itemId', 'trackId')
    streams_correct = streams.***TODO***(udfIsDurationCorrect('duration'))
    streams_cleaned = streams_correct.***TODO***(udfRemoveBraces('ts').alias('ts'), 'host', 'userId', 'trackId', streams_correct['duration'].cast('int'))
    streams_unique = streams_cleaned.***TODO***()

    # Store dataset
    streams_unique.saveAsTable("%s.stream" % database, mode="overwrite")

    # Finally, let Spark know that the job is done.
    sc.stop()
