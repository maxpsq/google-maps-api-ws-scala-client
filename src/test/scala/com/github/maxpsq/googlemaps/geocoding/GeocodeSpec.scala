package com.github.maxpsq.googlemaps.geocoding

import org.specs2.runner._
import org.specs2.mutable._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import com.github.maxpsq.googlemaps.GoogleParameters._
import Parameters._

class GeocodeSpec extends Specification with GeocodeCalls {
  
  implicit val client = new GeocodeClient()

  def ?(location: LocationParam) = callGeocode(location, Duration(6, SECONDS))

  "Geocode" should {
    
    "find data by LatLng" in {
      
      ?(LatLngParam(50.516196, 30.466651)) must beRight
      ?(LatLngParam(50.445057, 30.521049)) must beRight
      ?(LatLngParam(51.498685, -0.12967)) must beRight
    }  

    "find data by Address" in {
      ?(AddressParam("Via Montenapoleane, 2, Milan, Italy")) must beRight
      ?(AddressParam("Via Condotti, 3, Rome, Italy")) must beRight
      ?(AddressParam("Viale Nazzario Sauro, 2, Pavia, Italy")) must beRight
    }

    "raise an exception on too many OverQuota" in {
      var youGotAnException = false
      try {
      for ( i <- Range(1,15) )
          ?(AddressParam("Via Montenapoleane, 2, Milan, Italy")) must beLeft
      } catch {
        case th: Throwable => youGotAnException = true
      }    
      youGotAnException
    }
  }
}