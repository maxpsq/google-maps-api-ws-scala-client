package com.github.maxpsq.googlemaps.timezone

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import dispatch._
import scala.concurrent.ExecutionContext
import com.github.maxpsq.googlemaps._
import com.github.maxpsq.googlemaps.GoogleParameters._
import com.github.maxpsq.googlemaps.timezone.Parameters._

/**
 * TimezoneClient client implementation: 
 * defines a call to the WS and how to handle the response.
 * Just create an implicit instance of this and use one of 
 * the GeocodeCalls trait methods to perform a call.
 */
class TimezoneClient(http: Http, cpars: Seq[ClientParameter]) extends GoogleClient[Option[ResponseResult]](http, cpars) {
  
implicit val jsonReads: Reads[GoogleResponse[Option[ResponseResult]]] = (
    (__ \ 'dstOffset).readNullable[Int] ~
    (__ \ 'rawOffset).readNullable[Int] ~
    (__ \ 'status).read[String] ~   
    (__ \ 'timeZoneId).readNullable[String] ~
    (__ \ 'timeZoneName).readNullable[String] ~
    (__ \ 'error_message).readNullable[String]
  )( TimezoneResponse.apply(_:Option[Int], _:Option[Int], _:String, _:Option[String], _:Option[String], _:Option[String] ) )
    
  def this() = this(Http, Seq(NoSensor()))
  

  
  /**
   * This call to google service is limited
   * @see https://developers.google.com/maps/documentation/timezone/#Limits
   */
  def ?(location: LocationParam, epoch: TimestampParam)(implicit executionContext: ExecutionContext) = {

    import TimezoneClientConst._
    
    reqHandler( req <<? parameters(cpars ++ Seq(location, epoch) ) )
  }
}

/** 
 * Constant values to be injected into the client instance
 */
object TimezoneClientConst {
  
  val req = url("https://maps.googleapis.com") / "maps" / "api" / "timezone" / "json"
  
}



/** 
 * This trait is a collection methods that perform a call
 * to the Google TimeZone WebService.  
 * Just import an ExecutionContext and create an implicit TimezoneClient 
 * to call this functions.
 */
trait TimezoneCalls {
  
  import scala.concurrent.Await
  import scala.concurrent.duration._

  def callTimezone(loc: LocationParam, epoch: Long, d: Duration)(implicit ec: ExecutionContext, client: TimezoneClient) = {
    Await.result(client ?(loc, TimestampParam(epoch)), d)
  }
  
  def callTimezone(loc: LocationParam, epoch: Int, d: Duration)(implicit ec: ExecutionContext, client: TimezoneClient) = {
    Await.result(client ?(loc, TimestampParam(epoch.toLong)), d)
  }
    
}