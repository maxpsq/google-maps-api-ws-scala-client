package com.github.maxpsq.googlemaps.timezone

import play.api.libs.json._
import dispatch._
import scala.concurrent.ExecutionContext
import com.github.maxpsq.googlemaps._
import com.github.maxpsq.googlemaps.geocoding.Locations._

class Timezone(http: Http = Http) extends GoogleClient {

  /**
   * This call to google service is limited
   * @see https://developers.google.com/maps/documentation/timezone/#Limits
   */
  def ?(location: Location, timestamp: Long, language: String = "en")(implicit executionContext: ExecutionContext): Future[Either[Error, List[ResponseResult]]] = {
    import Timezone._
    http(req <<? List(location.tuple, "timestamp" -> timestamp.toString, "language" -> language) OK as.String).map {
      x =>
        val json = Json.parse(x)
        val response = json.validate[TimezoneResponse].map{ 
          case r: TimezoneResponse => r
        }.recoverTotal{
          e => throw new RuntimeException("Error while parsing JSON: "+ JsError.toFlatJson(e))
        }
        evalStatus(response.status, response.results)
    }
  }
}

object Timezone {
  val req = url("https://maps.googleapis.com") / "maps" / "api" / "timezone" / "json"
}