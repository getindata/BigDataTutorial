lyrics = sqlContext.sql("select * from tiger.lyrics")

from pyspark.ml.feature import Tokenizer
tokenizer = Tokenizer(inputCol="lyrics", outputCol="words")
lyricsTokenized = tokenizer.transform(lyrics)

lyricsTokenized.registerTempTable("lyrics")

sqlContext.sql("""
	select w.song_id, first(t.title), first(t.artistname), avg(h.happiness) from 
		(select song_id, explode(words) as word from lyrics) w,
		wordhappiness h, 
		track t 
	where w.word = h.word and w.song_id = t.id 
	group by w.song_id 
	order by avg(h.happiness) desc
	limit 10""").show()

# +-------+--------------------+-----------+------------------+
# |song_id|                 _c1|        _c2|               _c3|
# +-------+--------------------+-----------+------------------+
# |   4414|   Love Me Two Times|  The Doors| 6.087169777672246|
# |   3414|You're My Best Fr...|      Queen| 5.919306324396519|
# |   4844|Rock 'n' Roll Hig...|The Ramones| 5.910714255911963|
# |   1636|      Love Her Madly|  The Doors| 5.790440219003449|
# |   4385|            Dreaming|      Cream| 5.780606038642652|
# |   2008|       Play The Game|      Queen| 5.773666664759318|
# |   4354|   Hello, I Love You|  The Doors| 5.772745078685237|
# |   1339| The Best of My Love| The Eagles|5.7620338474289845|
# |    648|Shine on You Craz...| Pink Floyd|5.7446295641086715|
# |   1394|Gimme Gimme Shock...|The Ramones|  5.73999997228384|
# +-------+--------------------+-----------+------------------+
