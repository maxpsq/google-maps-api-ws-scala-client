package com.github.maxpsq.googlemaps.geocoding

import play.api.libs.json._
import play.api.libs.json.Reads._


case class AddressComponent(
    long_name: String, 
    short_name: String, 
    types: List[String]
)
object AddressComponent {
  implicit val jsonReads = Json.reads[AddressComponent]
}


case class Point(
    lat: Double,
    lng: Double
)
object Point {
  implicit val jsonReads = Json.reads[Point]  
}


case class Rectangle(
    northeast: Point,
    southwest: Point
)
object Rectangle {
  implicit val jsonReads = Json.reads[Rectangle]  
}


case class Geometry (
    bounds: Option[Rectangle],
    location: Point,
    location_type: String,
    viewport: Rectangle
)
object Geometry {
  implicit val jsonReads = Json.reads[Geometry]  
}


case class Result(
    address_components: List[AddressComponent], 
    formatted_address: String,
    geometry: Geometry,
    types: List[String],
    place_id: String
)
object Result {
  implicit val jsonReads = Json.reads[Result]
}


case class GeocodeResponse (
    results: List[Result]
)
object GeocodeResponse {
  implicit val jsonReads = Json.reads[GeocodeResponse] 
}