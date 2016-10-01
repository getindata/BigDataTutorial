package songs.streaming

import songs.Commons

case class UserActivity(serverUri: String, userId: Long, typeOfActivity: String, songId: Long, activityDuration: Long) {

  def isSongListenedOnPurpose() = {
    isSongListenedLongerThanAccidentalClick()
  }

  private def isSongListenedLongerThanAccidentalClick() = {
    activityDuration >= UserActivity.ACCIDENTAL_SONG_CLICK_DURATION
  }
}

object UserActivity {

  private val ACCIDENTAL_SONG_CLICK_DURATION = 10L

  def apply(eventTSV: String) = {
    val parsedEvent = eventTSV.split(Commons.TSV_SEPARATOR)
    new UserActivity(parsedEvent(0), parsedEvent(1).toLong, parsedEvent(2), parsedEvent(3).toLong, parsedEvent(4).toLong)
  }
}
