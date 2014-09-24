package com.github.maxpsq.googlemaps.timezone

import com.github.maxpsq.googlemaps.Parameter
import com.github.maxpsq.googlemaps.GeoSpatials._


object Parameters {

  // Location
  case class LocationParam(point: GeoPoint) extends Parameter("location", point) {
    override def isValid: Boolean = point.isValid
    override def toString: String = super.toString()
  }
  object LocationParam {    
    def apply(lat: Double, lng: Double): LocationParam = LocationParam(new GeoPoint(lat, lng))
  }

  
  // Timestamp
  case class TimestampParam(t: Long) extends Parameter("timestamp", t) {
    override def isValid: Boolean = true
    override def toString: String = super.toString()
  }

}