package com.github.maxpsq.googlemaps

abstract class Parameter(key: String, value: Any) {
  def isValid: Boolean
  def get: Any = value
  def tuple: Tuple2[String, String] = (key, value.toString)
  override def toString: String =  "%s=%s".format(key, value)
}

abstract class ClientParameter(key: String, value: Any) extends Parameter(key, value) 


object GoogleParameters {

  // Google API Key  
  case class ApiKeyParam (value: String) extends ClientParameter("apikey", value) {
    override def isValid: Boolean = true 
  }  
  
  
  // Language
  case class LanguageParam(value: String) extends ClientParameter("language", value) {
    override def isValid: Boolean = value.length() == 2 
  }
  
  object DefaultLang {
    def apply(): LanguageParam = LanguageParam("en")
  }

  
  // Sensor
  case class SensorParam(value: Boolean) extends ClientParameter("sensor", value) {
    override def isValid: Boolean = true 
  }
  
  object NoSensor {
    def apply(): SensorParam = SensorParam(false)
  }

}
