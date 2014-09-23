package com.github.maxpsq.googlemaps

object ResponseStatus {
  
  sealed trait Value 
  sealed trait KnownValue extends Value
  case class Unknown(override val toString: String) extends Value

  /** 
   *  indicates that no errors occurred and at least
   *  one result was returned.
   */
  object Ok extends KnownValue {
    override def toString = "OK"
  }
  
  /**
   *  indicates that the geocode was successful but returned no results. 
   *  This may occur if the WS was passed a non-existent address or a
   *  latlng in a remote location.
   */
  object ZeroResults extends KnownValue {
    override def toString = "ZERO_RESULTS"
  }
  
  /** indicates that you are over your quota. */
  object OverQueryLimit extends KnownValue {
    override def toString = "OVER_QUERY_LIMIT"
  }
  
  /** 
   * indicates that your request was denied, 
   * generally because of lack of a sensor parameter. 
   */
  object RequestDenied extends KnownValue {
    override def toString = "REQUEST_DENIED"
  }
  
  /** generally indicates that the query (address or latlng) is missing. */
  object InvalidRequest extends KnownValue {
    override def toString = "INVALID_REQUEST"
  }

  private val known = List(Ok, ZeroResults, OverQueryLimit, RequestDenied, InvalidRequest)

  def apply(s: String): Value = known.find(_.toString == s) getOrElse Unknown(s)
}
