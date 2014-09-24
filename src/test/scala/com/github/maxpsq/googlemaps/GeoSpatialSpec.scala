package com.github.maxpsq.googlemaps

import org.junit.runner._
import org.specs2.runner._
import org.specs2.mutable._
import org.specs2.mutable.SpecificationWithJUnit
import GeoSpatials._

@RunWith(classOf[JUnitRunner])
class GeoSpatialSpec extends SpecificationWithJUnit {
  
  "GeoSpatials enities " should {
    
    "    BE valid on CORRECT values" in {
      val lat = new Latitude(-12.143)
      val lng = new Longitude(14.1234)
      val point = new GeoPoint(lat,lng)
      lat.isValid must_== true 
      lng.isValid must_== true
      point.isValid must_== true
    }
    
    "NOT BE valid on INCORRECT values" in {
      val lat = new Latitude(-90.143)
      val lng = new Longitude(190.1234)
      val point = new GeoPoint(lat,lng)
      lat.isValid must_== false 
      lng.isValid must_== false
      point.isValid must_== false
    }
  }  

}