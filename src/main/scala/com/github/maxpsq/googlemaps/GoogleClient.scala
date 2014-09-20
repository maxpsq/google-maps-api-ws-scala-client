package com.github.maxpsq.googlemaps

import play.api.libs.json.JsValue
import play.api.libs.json.JsError
import play.api.libs.json.Reads
import dispatch._
import GoogleParameters._
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

case class GoogleResponse[T] (
    results: T, 
    status: ResponseStatus.Value,
    error_message: Option[String]
)


class GoogleClient[T](http: Http, cpars: Seq[ClientParameter]) {
  
  /** 
    * Creates a collection of tupled parameters
    * from a given set of Parameters, discarding those not set  
    * 
    * @param pars a set of Parameters  
    */
  def parameters(pars: Seq[Parameter]): Seq[Tuple2[String,String]] = {
    pars.map(_.tuple) 
  }

  
  /** 
    * Send a non-blocking HTTP request and handle the Response 
    * 
    * @param req a HTTP request  
    * @param ec    implicit ExecutionContext
    * @param reads implicit JSON deserializer
    */
  def reqHandler[T](req: Req)(implicit ec: ExecutionContext, reads: Reads[GoogleResponse[T]]): Future[Either[Error, T]] = {
    http(req OK as.String).map { x =>
      {
        try {
          val json = Json.parse(x)
          val response = jsonValidation[T](json)
          evalStatus(response)
        } catch {
          case e: JsonParsingException => Left(JsonParsingError(e.getMessage()))
        }
      }  
    }
  }  


  /** 
    * Validates the parsed JSON 
    * returns an Error or the data
    * 
    * @param json the parsed JSON object from Google WS  
    * @param reads implicit deserializer
    */
  private def jsonValidation[T](json: JsValue)(implicit reads: Reads[GoogleResponse[T]]): GoogleResponse[T] = { 
    json.validate[GoogleResponse[T]].map{ 
      case r: GoogleResponse[T] => r
    }.recoverTotal{
      e => throw new RuntimeException("Error while parsing JSON: "+ JsError.toFlatJson(e))
    }
  }
  
  
  /** 
    * Evaluates the status of a parsed response from the WS and
    * returns an Error or the data
    * 
    * @param response the parsed response from Google WS  
    */
  private def evalStatus[T](response: GoogleResponse[T]): Either[Error, T] = {
    response.status match {
      case ResponseStatus.ZeroResults ⇒ Left(ZeroResults)
      case ResponseStatus.OverQueryLimit ⇒ Left(OverQuotaLimit)
      case ResponseStatus.RequestDenied ⇒ Left(Denied)
      case ResponseStatus.InvalidRequest ⇒ Left(InvalidRequest)
      case _ ⇒ Right(response.results)
    }
  }
  
}