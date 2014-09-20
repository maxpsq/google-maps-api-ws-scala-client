package com.github.maxpsq.googlemaps.timezone

import org.junit.runner._
import org.specs2.runner._
import org.specs2.mutable._
import org.specs2.mutable.SpecificationWithJUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import com.github.maxpsq.googlemaps.GoogleParameters._
import Parameters._

@RunWith(classOf[JUnitRunner])
class TimezoneSpec extends SpecificationWithJUnit with TimezoneCalls {
  
  implicit val client: TimezoneClient = new TimezoneClient()

  def ?(x: LocationParam, t: Long) = callTimezone(x, t, Duration(3, SECONDS))
  
  "Timezone" should {
    
    "find data by location and timestamp (epoch)" in {
      
      val epoch = 198264918L
      
      val res1 = ?(LocationParam(50.516196, 30.466651), epoch)
      res1 must beRight
      
      val res2 = ?(LocationParam(50.445057, 30.521049), epoch)
      res2 must beRight
      
      val res3 = ?(LocationParam(51.498685, -0.12967), epoch)
      res3 must beRight

    }
  }
}