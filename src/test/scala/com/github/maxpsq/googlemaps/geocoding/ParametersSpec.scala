package com.github.maxpsq.googlemaps.geocoding

import org.specs2.runner._
import org.specs2.mutable._
import Parameters._

class ParametersSpec extends Specification {

  def parStr(lat: Double,lng: Double) = s"latlng=${lat},${lng}"  
  
  "Parameter LatLngParam " should {
    
    "    BE valid on CORRECT values" in {
      val lat = -12.143
      val lng = 14.1234
      val p1 = LatLngParam(lat,lng)
      p1.isValid must_== true
      p1.toString() must_== parStr(lat,lng)
      
      val p2 = LatLngParam(lat.toString,lng.toString)
      p2.isValid must_== true
      p2.toString() must_== parStr(lat,lng)
    }
    
    
    "NOT BE valid on INCORRECT values" in {
      val lat = -99.143
      val lng = 14.1234
      val p1 = LatLngParam(lat,lng)
      p1.isValid must_== false
      p1.toString() must_== parStr(lat,lng)
      
      val p2 = LatLngParam(lat.toString,lng.toString)
      p2.isValid must_== false
      p2.toString() must_== parStr(lat,lng)
    }
  }  
  
   
}