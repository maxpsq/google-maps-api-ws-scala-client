package com.github.maxpsq.googlemaps.timezone

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import com.github.maxpsq.googlemaps.ResponseStatus


case class ResponseResult(
   dstOffset: Option[Int],
   rawOffset: Option[Int],
   timeZoneId: Option[String],
   timeZoneName: Option[String]
)

object ResponseResult {
  implicit val jsonReads = Json.reads[ResponseResult]
}

/* 
 * Even if only one result is expected, I wrap it in a List.
 * This is an attempt to Normalize the Response structure of all the WSs.
 */
case class TimezoneResponse(
    results: List[ResponseResult], 
    status: ResponseStatus.Value
)

object TimezoneResponse {
  implicit val jsonReads: Reads[TimezoneResponse] = (
    (__ \ 'dstOffset).readNullable[Int] ~
    (__ \ 'rawOffset).readNullable[Int] ~
    (__ \ 'status).read[String] ~   
    (__ \ 'timeZoneId).readNullable[String] ~
    (__ \ 'timeZoneName).readNullable[String] 
  )( TimezoneResponse.apply(_:Option[Int], _:Option[Int], _:String, _:Option[String], _:Option[String] ) )
  
  def apply(dst: Option[Int], raw: Option[Int], status: String, tzID: Option[String], tzn: Option[String]): TimezoneResponse = {
    TimezoneResponse(List(ResponseResult(dst, raw, tzID, tzn)), ResponseStatus(status) ) 
  }
    
}
