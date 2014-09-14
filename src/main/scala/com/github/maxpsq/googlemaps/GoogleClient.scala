package com.github.maxpsq.googlemaps

import play.api.libs.json.JsValue
import play.api.libs.json.JsError
import play.api.libs.json.Reads

trait GoogleClient {

  def evalStatus[T](status: ResponseStatus.Value, result: T) = {
    status match {
      case ResponseStatus.ZeroResults ⇒ Left(ZeroResults)
      case ResponseStatus.OverQueryLimit ⇒ Left(OverQuotaLimit)
      case ResponseStatus.RequestDenied ⇒ Left(Denied)
      case ResponseStatus.InvalidRequest ⇒ Left(InvalidRequest)
      case _ ⇒ Right(result)
    }
  }
  
}