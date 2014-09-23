package com.github.maxpsq.googlemaps.timezone

import play.api.libs.json._
import play.api.libs.json.Reads._

case class TimezoneResponse (
    dstOffset: Int,
    rawOffset: Int,
    timeZoneId: String,
    timeZoneName: String
)

object TimezoneResponse {
  implicit val jsonReads = Json.reads[TimezoneResponse] 
}
