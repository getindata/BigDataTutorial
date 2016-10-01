package songs

case class TopSong(songId: Long, timePlayedInSeconds: Long) {
  override def toString() = {
    s"$songId${Commons.TSV_SEPARATOR}$timePlayedInSeconds"
  }
}

object Commons {
  val TSV_SEPARATOR = "\t"
  val CSV_SEPARATOR = ","
}
