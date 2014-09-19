package com.github.maxpsq.googlemaps.timezone

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import com.github.maxpsq.googlemaps.ResponseStatus

case class ResponseResult (
    dstOffset: Int,
    rawOffset: Int,
    timeZoneId: String,
    timeZoneName: String
)

case class TimezoneResponse(
    results: List[ResponseResult], 
    status: ResponseStatus.Value,
    error_message: Option[String]
)

object TimezoneResponse {
  implicit val jsonReads: Reads[TimezoneResponse] = (
    (__ \ 'dstOffset).readNullable[Int] ~
    (__ \ 'rawOffset).readNullable[Int] ~
    (__ \ 'status).read[String] ~   
    (__ \ 'timeZoneId).readNullable[String] ~
    (__ \ 'timeZoneName).readNullable[String] ~
    (__ \ 'error_message).readNullable[String]
  )( TimezoneResponse.apply(_:Option[Int], _:Option[Int], _:String, _:Option[String], _:Option[String], _:Option[String] ) )
  
  def apply(dst: Option[Int], raw: Option[Int], status: String, tzID: Option[String], tzn: Option[String], errMsg: Option[String]): TimezoneResponse = {
    val respStatus = ResponseStatus(status)
    respStatus.toString match {
      case "OK" => TimezoneResponse(List(ResponseResult(dst.get, raw.get, tzID.get, tzn.get)), respStatus, errMsg )
      case _ => TimezoneResponse(List(), respStatus, errMsg )
    }
     
  }
    
}
