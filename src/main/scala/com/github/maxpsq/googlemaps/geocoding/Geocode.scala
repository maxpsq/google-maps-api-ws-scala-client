package com.github.maxpsq.googlemaps.geocoding

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import dispatch._
import scala.concurrent.ExecutionContext
import com.github.maxpsq.googlemaps._
import com.github.maxpsq.googlemaps.GoogleParameters._
import com.github.maxpsq.googlemaps.geocoding.Parameters._

/**
 * Geocode client implementation: 
 * defines a call to the WS and how to handle the response.
 * Just create an implicit instance of this and use one of 
 * the GeocodeCalls trait methods to perform a call.
 */
class GeocodeClient(http: Http, cpars: Seq[ClientParameter]) extends GoogleClient[List[ResponseResult]](http, cpars) {

  implicit val jsonReads: Reads[GoogleResponse[List[ResponseResult]]] = (
    (__ \ 'results).read[List[ResponseResult]] ~
    (__ \ 'status).read[String] ~
    (__ \ 'error_message).readNullable[String] 
  )( GeocodeResponse.apply( _: List[ResponseResult], _: String, _: Option[String]) ) 
  
  def this() = this(Http, Seq(NoSensor()))
  
  /**
   * This call to google service is limited
   * @see https://developers.google.com/maps/documentation/geocoding/#Limits
   */
  def ?(location: LocationParam)(implicit executionContext: ExecutionContext): Future[Either[Error, List[ResponseResult]]] = {
    
    import GeocodeClient._
    
    reqHandler( req <<? parameters(cpars :+ location) )    
  }

}

/** 
 * Constant values to be injected into the client instance
 */
object GeocodeClient {
  val req = url("http://maps.googleapis.com") / "maps" / "api" / "geocode" / "json"
}



/** 
 * This trait is a collection methods that perform a call
 * to the Google geocode WebService.  
 * Just import an ExecutionContext and create an implicit GeocodeClient 
 * to call this functions.
 */
trait GeocodeCalls {
  
  import scala.concurrent.Await
  import scala.concurrent.duration._

  def callGeocode(l: LocationParam, d: Duration)(implicit ec: ExecutionContext, client: GeocodeClient): Either[Error, List[ResponseResult]] = {
    Await.result(client ? l, d) 
  }
  
}