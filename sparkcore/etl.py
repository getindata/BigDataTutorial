from pyspark import SparkConf, SparkContext
import sys
import getpass


def is_duration_correct(duration):
    return duration != 'INF' and 30 <= int(duration) and int(duration) <= 1200


def remove_braces(text):
    return text[1:-1]


if __name__ == "__main__":

    # Ensure that an input and output are specified on the command line
    if len(sys.argv) < 3:
        print('Usage: ' + sys.argv[0] + ' <in> <out> [<dump>]')
        sys.exit(1)

    # Grab the input and output
    input = sys.argv[1]
    output = sys.argv[2]
    debug = sys.argv[3] if sys.argv[3] is not None else False

    # Create a context for the job. The context is used to manage the job at a high level.
    appName = "ETL-%s" % (getpass.getuser())
    sc = SparkContext(appName = appName)

    # Read in the dataset
    logs = sc.textFile(input)
    records = logs.map(lambda line: line.split('\t'))
    streams = records.filter(lambda record: record[3] == 'SongPlayed')
    streams_projected = streams.map(lambda record: (remove_braces(record[0]), record[1], record[2], record[4], record[5]))
    streams_correct = streams_projected.filter(lambda (timestamp, host, userId, songId, duration): is_duration_correct(duration))
    streams_unique = streams_correct.distinct()

    # Save the results in the specified output directory.
    streams_unique.saveAsTextFile(output)

    if dump:
        # Dump content for debugging purposes
        output = streams_unique.collect()
        for record in output :
            print(record)

    # Finally, let Spark know that the job is done.
    sc.stop()
