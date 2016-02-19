lyrics = sqlContext.sql("select * from tiger.lyrics")
happiness = sqlContext.sql("select * from wordhappiness")
tracks = sqlContext.sql("select * from track")

from pyspark.ml.feature import Tokenizer
tokenizer = Tokenizer(inputCol="lyrics", outputCol="words")
lyricsTokenized = tokenizer.transform(lyrics)

# flatten lyricsTokenized
wordSongPairs = lyricsTokenized.rdd.flatMap(lambda r : [(w, r.song_id) for w in r.words])

# join words with happiness
idHappinessPairs = wordSongPairs.join(happiness.rdd).map(lambda (word, id_happ) : id_happ)

# calculate average happiness for every song_id
idAverageHappinessPairs = idHappinessPairs.mapValues(lambda x: (x, 1)).reduceByKey(lambda x, y: (x[0] + y[0], x[1] + y[1])).mapValues(lambda (s, c) : s/c)

# find 10 most sad songs
top10ids = idAverageHappinessPairs.takeOrdered(10, lambda (id, happ) : happ)

# find name of most sad song
tracks.rdd.filter(lambda x : x[0] == top10ids[0][0]).collect()
