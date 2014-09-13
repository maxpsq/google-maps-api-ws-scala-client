package com.github.maxpsq.googlemaps.geocoding

import play.api.libs.json._
import dispatch._
import scala.concurrent.ExecutionContext

class Geocode(http: Http = Http) {
  import Geocode._

  /**
   * This call to google service is limited
   * @see https://developers.google.com/maps/documentation/geocoding/#Limits
   */
  def ?(location: Location)(implicit executionContext: ExecutionContext): Future[Either[Error, List[ResponseResult]]] = {
    import Geocode._
    //val location = ("latlng", "%s,%s".format(latitude, longitude))
    val req = url(googleapis) / "maps" / "api" / "geocode" / "json"
    http(req <<? List(location.toParam, "sensor" -> "false") OK as.String).map {
      x =>
        val json = Json.parse(x)
        val response = json.validate[GeocodeResponse].get
        response.status match {
          case ResponseStatus.ZeroResults ⇒ Left(ZeroResults)
          case ResponseStatus.OverQueryLimit ⇒ Left(OverQuotaLimit)
          case ResponseStatus.RequestDenied ⇒ Left(Denied)
          case ResponseStatus.InvalidRequest ⇒ Left(InvalidRequest)
          case _ ⇒ Right(response.results)
        }
    }
  }
}

object Geocode {
  val googleapis = "http://maps.googleapis.com"
}