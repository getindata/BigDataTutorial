// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package songs.streaming

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.windowing.time.Time
import songs.TopSong

object TopSongs {

  def topSongs(events: DataStream[String], window: Time): DataStream[TopSong] = {
    events
      .map(event => UserActivity.apply(event))
      .filter(userActivity => userActivity.isSongListenedOnPurpose())
      .map(listenedOnPurposeSong => (listenedOnPurposeSong.songId, listenedOnPurposeSong.activityDuration))
      .keyBy(0)
      .timeWindow(window)
      .sum(1)
      .map(topSong => new TopSong(topSong._1, topSong._2))
  }
}
