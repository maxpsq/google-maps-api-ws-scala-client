package com.github.maxpsq.googlemaps.timezone

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
class TimezoneClient(http: Http, cpars: Seq[ClientParameter]) extends GoogleClient[TimezoneResponse](http, cpars) {

  def this() = this(Http, Seq(NoSensor()))
  
  def ?(location: LocationParam, epoch: TimestampParam)(implicit executionContext: ExecutionContext): Future[Either[Error, TimezoneResponse]]  = {

    import TimezoneClientConst._
    
    validateParameters( cpars ++ Seq(location, epoch) ).right.flatMap{ checkedPars =>
      reqHandler[TimezoneResponse]( req <<? parameters( checkedPars ) ) 
    }    
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

  private val usageLimits = UsageLimits(10, Duration(1, SECONDS))
  
  private val throttler = new Throttler(usageLimits)
  
  /**
   * This call to google service is limited
   * @see https://developers.google.com/maps/documentation/timezone/#Limits
   */
  def callTimezone(loc: LocationParam, epoch: Long, d: Duration)(implicit ec: ExecutionContext, client: TimezoneClient)
  : Either[Error, TimezoneResponse] = {
    throttler.obeyLimits{ Await.result(client ?(loc, TimestampParam(epoch)), d) }
  }
  
  def callTimezone(loc: LocationParam, epoch: Int,  d: Duration)(implicit ec: ExecutionContext, client: TimezoneClient)
  : Either[Error, TimezoneResponse] = {
    callTimezone(loc, epoch.toLong, d)
  }
    
}