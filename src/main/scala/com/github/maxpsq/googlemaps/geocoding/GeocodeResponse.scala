package com.github.maxpsq.googlemaps.geocoding

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import com.github.maxpsq.googlemaps.ResponseStatus


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


case class GeocodeResponse (
    results: List[ResponseResult], 
    status: ResponseStatus.Value
)  

object GeocodeResponse {
  
  implicit val jsonReads: Reads[GeocodeResponse] = (
    (__ \ 'results).read[List[ResponseResult]] ~
    (__ \ 'status).read[String] 
  )( GeocodeResponse.apply( _: List[ResponseResult], _: String) ) 
  
  def apply(results: List[ResponseResult], status: String): GeocodeResponse =
    GeocodeResponse(results, ResponseStatus(status) )
}
