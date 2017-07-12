package com.github.maxpsq.googlemaps.timezone

import org.specs2.runner._
import org.specs2.mutable._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.collection.mutable
import com.github.maxpsq.googlemaps.GoogleParameters._
import com.github.maxpsq.googlemaps._
import Parameters._

class TimezoneSpec extends Specification with TimezoneCalls {
  
  implicit val client: TimezoneClient = new TimezoneClient()

  def ?(x: LocationParam, t: Long) = callTimezone(x, t, Duration(10, SECONDS))

  val epoch = 198264918L  
  
  "Timezone" should {
    
    "find data by location and timestamp (epoch)" in {
      
      val res1 = ?(LocationParam(50.516196, 30.466651), epoch)      
      val res2 = ?(LocationParam(50.445057, 30.521049), epoch)
      val res3 = ?(LocationParam(51.498685, -0.12967), epoch)
      res1 must beRight
      res2 must beRight
      res3 must beRight
      
    }
    
  }
  
}