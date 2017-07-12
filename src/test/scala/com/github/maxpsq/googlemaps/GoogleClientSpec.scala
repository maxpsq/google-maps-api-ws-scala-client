package com.github.maxpsq.googlemaps

import org.junit.runner._
import org.specs2.runner._
import org.specs2.mutable._
import org.specs2.mutable.SpecificationWithJUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import com.github.maxpsq.googlemaps.GoogleParameters._
import com.github.maxpsq.googlemaps.geocoding.Parameters._
import dispatch.Http

@RunWith(classOf[JUnitRunner])
class GoogleClientSpec extends SpecificationWithJUnit {
  
  val client = new GoogleClient(Http.default, Seq(LanguageParam("en")))

  "GoogleClient paramList()" should {
    
    "return a list of parameters" in {
      
      client.parameters( Seq(
          LatLngParam(50.445057, 30.521049), 
          LatLngParam(51.498685, -0.12967),
          LanguageParam("en")
      ) ) must beAnInstanceOf[Seq[Tuple2[String,String]]]
    } 
  }  
  
}