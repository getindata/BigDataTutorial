package songs.streaming

import songs.Commons

case class UserActivity(ts: String, serverUri: String, userId: Long, typeOfActivity: String, songId: Long, activityDuration: Long) {

  def isSongListenedOnPurpose() = {
    isSongListenedLongerThanAccidentalClick()
  }

  private def isSongListenedLongerThanAccidentalClick() = {
    activityDuration >= UserActivity.ACCIDENTAL_SONG_CLICK_DURATION
  }

  private def isSongListened() = {
    typeOfActivity == "SongPlayed"
  }
}

object UserActivity {

  private val ACCIDENTAL_SONG_CLICK_DURATION = 10L

  def apply(eventTSV: String) = {
    val parsedEvent = eventTSV.split(Commons.TSV_SEPARATOR)
    new UserActivity(parsedEvent(0), parsedEvent(1), parsedEvent(2).toLong, parsedEvent(3), parsedEvent(4).toLong, parsedEvent(5).toLong)
  }
}
