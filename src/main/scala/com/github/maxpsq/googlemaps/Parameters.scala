package com.github.maxpsq.googlemaps

class Parameter(key: String, value: Any) {  
  def tuple: Tuple2[String, String] = (key, value.toString)
  override def toString: String =  "%s=%s".format(key, value)
}

class ClientParameter(key: String, value: Any) extends Parameter(key, value) 


object GoogleParameters {

  // Google API Key  
  case class ApiKeyParam (value: String) extends ClientParameter("apikey", value)  
  
  
  // Language
  case class LanguageParam(value: String) extends ClientParameter("language", value)
  
  object DefaultLang {
    def apply(): LanguageParam = LanguageParam("en")
  }

  
  // Sensor
  case class SensorParam(value: Boolean) extends ClientParameter("sensor", value)
  
  object NoSensor {
    def apply(): SensorParam = SensorParam(false)
  }

}
