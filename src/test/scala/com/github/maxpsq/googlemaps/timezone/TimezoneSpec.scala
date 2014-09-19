package com.github.maxpsq.googlemaps.timezone

import org.junit.runner._
import org.specs2.runner._
import org.specs2.mutable._
import org.specs2.mutable.SpecificationWithJUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class TimezoneSpec extends SpecificationWithJUnit {
  "Timezone" should {
    "find data by location and timestamp (epoch)" in {
      val client = new Timezone()

      def ?(x: Location, t: Long) = Await.result(client ?(x, t), Duration(3, SECONDS))

      val epoch = 198264918
      
      ?(Location(50.516196, 30.466651), epoch) must beRight
      ?(Location(50.445057, 30.521049), epoch) must beRight
      ?(Location(51.498685, -0.12967), epoch) must beRight

    }
  }
}