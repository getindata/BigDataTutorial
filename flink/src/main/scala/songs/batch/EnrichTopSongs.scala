package songs.batch

import org.apache.flink.api.common.operators.Order
import songs.TopSong

case class EnrichedTopSong(songId: Long, totalDuration: Long, isGonnaBeTeenageHit: Boolean)

object EnrichTopSongs {

  import org.apache.flink.api.scala._

  private val MIN_TOTAL_DURATION = 1000L
  private val NUMBER_OF_TOP_SONGS_TO_BE_FETCHED = 10
  private val OLD_SONG_ID_THRESHOLD = 1000L

  def getOnlyHits(topSongs: DataSet[TopSong]) = {
    topSongs
      .filter(topSong => topSong.timePlayedInSeconds >= MIN_TOTAL_DURATION)
      .sortPartition("timePlayedInSeconds", Order.DESCENDING)
      .first(NUMBER_OF_TOP_SONGS_TO_BE_FETCHED)
  }

  def enrichHits(hits: DataSet[TopSong]) = {
    hits
      .map(hit => new EnrichedTopSong(hit.songId, hit.timePlayedInSeconds, markIfSongGonnaBeTeenageHit(hit)))
  }

  private def markIfSongGonnaBeTeenageHit(hit: TopSong) = {
    hit.songId > OLD_SONG_ID_THRESHOLD
  }
}
