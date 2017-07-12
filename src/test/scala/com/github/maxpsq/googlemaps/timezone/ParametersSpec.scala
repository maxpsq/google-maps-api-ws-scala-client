package com.github.maxpsq.googlemaps.timezone

import org.specs2.runner._
import org.specs2.mutable._
import Parameters._
import com.github.maxpsq.googlemaps.GeoSpatials._

class ParametersSpec extends Specification {

  "Parameter LocationParam " should {
    
    def parStr(lat: Double,lng: Double) = s"location=${lat},${lng}"

    "    BE valid on CORRECT values" in {
      val lat = -12.143
      val lng = 14.1234
      val p1 = LocationParam(lat,lng)
      p1.isValid must_== true
      p1.toString() must_== parStr(lat,lng)
      
      val p2 = LocationParam(new GeoPoint(lat,lng))
      p2.isValid must_== true
      p2.toString() must_== parStr(lat,lng)
    }
    
    
    "NOT BE valid on INCORRECT values" in {
      val lat = -99.143
      val lng = 14.1234
      val p1 = LocationParam(lat,lng)
      p1.isValid must_== false
      p1.toString() must_== parStr(lat,lng)
      
      val p2 = LocationParam(new GeoPoint(lat,lng))
      p2.isValid must_== false
      p2.toString() must_== parStr(lat,lng)
    }
    
  }  
  
   
  "Parameter TimestampParam " should {
    
    def parStr(t: Long) = "timestamp="+t.toString    
    
    "    BE valid on CORRECT values" in {
      val t: Long = 198659824L
      val p1 = TimestampParam(t)
      p1.isValid must_== true
      p1.toString() must_== parStr(t)
      
    }
    
  }
  
  
}