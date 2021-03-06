package com.github.maxpsq.googlemaps.geocoding

import com.github.maxpsq.googlemaps.Parameter
import com.github.maxpsq.googlemaps.GeoSpatials._

object Parameters {

  sealed trait LocationParam extends Parameter
  
  // LatLng
  case class LatLngParam(lat: Double, lng: Double) extends Parameter("latlng", new GeoPoint(lat, lng)) with LocationParam {
    override def isValid: Boolean = super.get.asInstanceOf[GeoPoint].isValid
    override def toString: String = super.toString()
  }
  object LatLngParam {
    def apply(latitude: String, longitude: String): LatLngParam =
      LatLngParam(latitude.toDouble, longitude.toDouble)
  }

  
  // Address
  case class AddressParam(address: String) extends Parameter("address", address) with LocationParam {
    override def isValid: Boolean = true
  }

}