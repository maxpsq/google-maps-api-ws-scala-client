package com.github.maxpsq.googlemaps.geocoding

import org.junit.runner._
import org.specs2.runner._
import org.specs2.mutable._
import org.specs2.mutable.SpecificationWithJUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._
import com.github.maxpsq.googlemaps.geocoding.Locations._

@RunWith(classOf[JUnitRunner])
class GeocodeSpec extends SpecificationWithJUnit {
  "Geocode" should {
    "find data by location" in {
      val client = new Geocode()

      def ?(location: Location) = Await.result(client ? location, Duration(3, SECONDS))

      ?(GeoPoint(50.516196, 30.466651)) must beRight
      ?(GeoPoint(50.445057, 30.521049)) must beRight
      ?(GeoPoint(51.498685, -0.12967)) must beRight

      ?(Address("Via Montenapoleane, 2 Milano, Italia")) must beRight
      ?(Address("Via Condotti, 3 Roma, Italia")) must beRight
      ?(Address("Viale Nazzario Sauro, 2 Pavia, Italia")) must beRight
    }
  }
}