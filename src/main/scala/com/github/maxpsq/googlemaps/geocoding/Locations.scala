package com.github.maxpsq.googlemaps.geocoding

import com.github.maxpsq.googlemaps.Parameter

object Locations {

  sealed trait Location extends Parameter {
    override def tuple: Tuple2[String, String]
  }

  case class GeoPoint(latitude: Double, longitude: Double) extends Location {
    override def tuple = ("latlng", "%s,%s".format(latitude, longitude))
  }

  case class Address(address: String) extends Location {
    override def tuple = ("address", address)
  }

  object GeoPoint {
    def apply(latitude: String, longitude: String): GeoPoint =
      GeoPoint(latitude.toDouble, longitude.toDouble)
  }

}