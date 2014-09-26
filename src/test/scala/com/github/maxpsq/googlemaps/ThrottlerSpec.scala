package com.github.maxpsq.googlemaps

import org.junit.runner._
import org.specs2.runner._
import org.specs2.mutable._
import org.specs2.mutable.SpecificationWithJUnit
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class ThrottlerSpec extends SpecificationWithJUnit {
  
  val usageLimits = UsageLimits(4, Duration(20, SECONDS))
  val throttler = new Throttler(usageLimits)
  def printer(now: Long) = println("now is " + now)
  

  " a" should {
    
    "s" in {
      throttler.obeyLimits(printer(System.currentTimeMillis()))
      throttler.obeyLimits(printer(System.currentTimeMillis()))
      throttler.obeyLimits(printer(System.currentTimeMillis()))
      usageLimits.rate must_== 5000L
    }
  }
  
}