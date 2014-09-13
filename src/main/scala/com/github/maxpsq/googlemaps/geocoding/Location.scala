package com.github.maxpsq.googlemaps.geocoding

sealed trait Location {
  def toParam: Tuple2[String,String]
}

case class GeoPoint(latitude: Double, longitude: Double) extends Location {
  override def toParam = ("latlng", "%s,%s".format(latitude, longitude))
}

case class Address(address: String) extends Location{
  override def toParam = ("address", address)
}

object GeoPoint {
  def apply(latitude: String, longitude: String): GeoPoint =
    GeoPoint(latitude.toDouble, longitude.toDouble)
}