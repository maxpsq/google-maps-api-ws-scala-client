package com.github.maxpsq.googlemaps

import org.specs2.runner._
import org.specs2.mutable._
import GoogleParameters._

class ParametersSpec extends Specification {

  def parStr(t: Boolean) = s"sensor="+t.toString()  
  
  "Parameter SensorParam " should {
    
    "    BE valid on CORRECT values" in {
      val t = true
      val f = false
      
      val p1 = SensorParam(t)
      p1.isValid must_== true
      p1.toString() must_== parStr(t)
      
      val p2 = NoSensor()
      p2.isValid must_== true
      p2.toString() must_== parStr(f)
    }
    
  }  
  
   
}