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


case class ResponseResult(
    address_components: List[AddressComponent], 
    formatted_address: String,
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
