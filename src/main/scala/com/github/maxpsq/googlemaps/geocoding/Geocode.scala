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
class GeocodeClient(http: Http, cpars: Seq[ClientParameter]) extends GoogleClient[GeocodeResponse](http, cpars) {

  def this() = this(Http.default, Seq(NoSensor()))
  
  def ?(location: LocationParam)(implicit executionContext: ExecutionContext): Future[Either[Error, GeocodeResponse]] = {
    
    import GeocodeClient._
    
    validateParameters( cpars :+ location ).right.flatMap{ checkedPars =>
      reqHandler[GeocodeResponse]( req <<? parameters(checkedPars) )    
    }
  }
}

/** 
 * Constant values to be injected into the client instance
 */
object GeocodeClient {
  val req = url("https://maps.googleapis.com") / "maps" / "api" / "geocode" / "json"
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

  /**
   * This call to google service is limited
   * @see https://developers.google.com/maps/documentation/geocoding/#Limits
   */
  def callGeocode(l: LocationParam, d: Duration)
                 (implicit ec: ExecutionContext, client: GeocodeClient)
                 : Either[Error, GeocodeResponse] = {
    Await.result(client ? l, d)  
  }
  
}