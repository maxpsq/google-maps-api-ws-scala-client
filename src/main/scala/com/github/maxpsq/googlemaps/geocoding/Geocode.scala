package com.github.maxpsq.googlemaps.geocoding

import play.api.libs.json._
import dispatch._
import scala.concurrent.ExecutionContext
import com.github.maxpsq.googlemaps._
import com.github.maxpsq.googlemaps.geocoding.Locations._

class Geocode(http: Http = Http) extends GoogleClient {

  /**
   * This call to google service is limited
   * @see https://developers.google.com/maps/documentation/geocoding/#Limits
   */
  def ?(location: Location, language: String = "en")(implicit executionContext: ExecutionContext): Future[Either[Error, List[ResponseResult]]] = {
    import Geocode._
    http(req <<? List(location.tuple, "sensor" -> "false", "language" -> language) OK as.String).map {
      x =>
        val json = Json.parse(x)
        val response = json.validate[GeocodeResponse].map{ 
          case r: GeocodeResponse => r
        }.recoverTotal{
          e => throw new RuntimeException("Error while parsing JSON: "+ JsError.toFlatJson(e))
        }

        evalStatus(response.status, response.results)
    }
  }
}

object Geocode {
  val req = url("http://maps.googleapis.com") / "maps" / "api" / "geocode" / "json"
}