package com.github.maxpsq.googlemaps.geocoding

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
class GeocodeClient(http: Http, cpars: Seq[ClientParameter]) extends GoogleClient[List[Result]](http, cpars) {

  def this() = this(Http, Seq(NoSensor()))
  
  /**
   * This call to google service is limited
   * @see https://developers.google.com/maps/documentation/geocoding/#Limits
   */
  def ?(location: LocationParam)(implicit executionContext: ExecutionContext): Future[Either[Error, List[Result]]] = {
    
    import GeocodeClient._
    
    reqHandler[List[Result]]( req <<? parameters(cpars :+ location) )    
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

  def callGeocode(l: LocationParam, d: Duration)(implicit ec: ExecutionContext, client: GeocodeClient): Either[Error, List[Result]] = {
    Await.result(client ? l, d) 
  }
  
}