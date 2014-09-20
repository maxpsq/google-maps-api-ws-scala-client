package com.github.maxpsq.googlemaps


sealed trait Error
case object ZeroResults    extends Error
case object OverQuotaLimit extends Error
case object Denied         extends Error
case object InvalidRequest extends Error
case class  JsonParsingError(msg: String) extends Error


case class JsonParsingException(msg: String) extends Exception(msg) 