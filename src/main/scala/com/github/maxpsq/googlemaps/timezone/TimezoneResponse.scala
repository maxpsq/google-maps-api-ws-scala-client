package com.github.maxpsq.googlemaps.timezone

import com.github.maxpsq.googlemaps.ResponseStatus
import com.github.maxpsq.googlemaps.GoogleResponse

case class ResponseResult (
    dstOffset: Int,
    rawOffset: Int,
    timeZoneId: String,
    timeZoneName: String
)

object TimezoneResponse {

  def apply(dst: Option[Int], raw: Option[Int], status: String, tzID: Option[String], tzn: Option[String], errMsg: Option[String]): GoogleResponse[Option[ResponseResult]] = {
    val respStatus = ResponseStatus(status)
    respStatus.toString match {
      case "OK" => GoogleResponse[Option[ResponseResult]](Some(ResponseResult(dst.get, raw.get, tzID.get, tzn.get)), respStatus, errMsg )
      case _ => GoogleResponse[Option[ResponseResult]](None, respStatus, errMsg )
    }
     
  }
    
}
