package com.github.maxpsq.googlemaps.geocoding

import play.api.libs.json._
import play.api.libs.json.Reads._
import com.github.maxpsq.googlemaps.ResponseStatus
import com.github.maxpsq.googlemaps.GoogleResponse


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


case class ResponseResult(
    address_components: List[AddressComponent], 
    formatted_address: String,
    geometry: Geometry,
    types: List[String]
)
object ResponseResult {
  implicit val jsonReads = Json.reads[ResponseResult]
}


object GeocodeResponse {

  def apply(results: List[ResponseResult], status: String, errMsg: Option[String]): GoogleResponse[List[ResponseResult]] = {
    GoogleResponse[List[ResponseResult]](results, ResponseStatus(status), errMsg )
  }
  
}
