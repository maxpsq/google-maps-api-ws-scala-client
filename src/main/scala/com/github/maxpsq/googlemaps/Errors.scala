package com.github.maxpsq.googlemaps


sealed trait Error
case class ZeroResults(msg: String)     extends Error
case class OverQuotaLimit(msg: String)  extends Error
case class Denied(msg: String)          extends Error
case class InvalidRequest(msg: String)  extends Error
case class UnhandledStatus(msg: String) extends Error
case class JsonParsingError(msg: String) extends Error
case class InvalidParameterError(par: Parameter) extends Error
case class HttpError(msg: String) extends Error
