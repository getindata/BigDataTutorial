package songs.batch

import org.apache.flink.api.scala.ExecutionEnvironment
import songs.{Commons, TopSong}

object BatchFlink {

  def main(args: Array[String]):Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment

    val tsvFileLines = env.readTextFile("top_songs.txt")

    import org.apache.flink.api.scala._

    val topSongs = tsvFileLines
        .map { line =>
          val values = line.split(Commons.CSV_SEPARATOR)
          new TopSong(values(0).toLong, values(1).toLong)
        }

    val hits = EnrichTopSongs.getOnlyHits(topSongs)

    val enrichedHits = EnrichTopSongs.enrichHits(hits)

    val onlyTeenageHits = enrichedHits.filter(_.isGonnaBeTeenageHit)
    hits.print()
    enrichedHits.print()
    onlyTeenageHits.print()

  }
}
