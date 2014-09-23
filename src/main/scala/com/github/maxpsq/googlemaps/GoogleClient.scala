package com.github.maxpsq.googlemaps

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import play.api.libs.json._
import dispatch._
import GoogleParameters._


class GoogleClient[T](http: Http, cpars: Seq[ClientParameter]) {
  
  case class StatusResponse (
      status: ResponseStatus.Value,
      error_message: Option[String]
  )
  object StatusResponse {
    import play.api.libs.functional.syntax._
    
    implicit val jsonReads: Reads[StatusResponse] = (
      (__ \ 'status).read[String] ~
      (__ \ 'error_message).readNullable[String] 
    )( StatusResponse.apply(_: String, _:Option[String]) ) 
  
    def apply(status: String, errMsg: Option[String]): StatusResponse = {
      StatusResponse(ResponseStatus(status), errMsg)
    }
  }

  
  /** 
    * Creates a collection of tupled parameters
    * from a given set of Parameters  
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
  def reqHandler[T](req: Req)(
      implicit ec: ExecutionContext, 
               statusReads: Reads[StatusResponse], 
               dataReads: Reads[T]
  ): Future[Either[Error, T]] = {
    
    def statusExtractor(json: JsValue) = readsEither[StatusResponse](json)
    def dataExtractor(json: JsValue) = readsEither[T](json)
    
    http(req OK as.String).map { x =>
      val json = Json.parse(x)
      statusExtractor(json).right.flatMap{ sr => 
        evalStatus(sr.status).right.flatMap{ ok =>
          dataExtractor(json)  
        }
      }
    }
  }  

  
  /** 
    * Validates the parsed JSON. 
    * Returns an Error or the data.
    * 
    * @param json the parsed JSON object from Google WS  
    * @param reads implicit deserializer
    */
  def readsEither[Z](json: JsValue)(implicit rds: Reads[Z]): Either[Error, Z] = { 
    rds.reads(json).map(Right(_)).recoverTotal { e => 
      Left(JsonParsingError(JsError.toFlatJson(e).toString))
    }
  }
  
  /**
   * Converts a ResponseStatus into a specific Error
   */
  private def evalStatus(status: ResponseStatus.Value): Either[Error, _] = {
    status match {
      case ResponseStatus.Ok => Right("Ok")
      case ResponseStatus.ZeroResults => Left(ZeroResults)
      case ResponseStatus.OverQueryLimit => Left(OverQuotaLimit)
      case ResponseStatus.RequestDenied => Left(Denied)
      case ResponseStatus.InvalidRequest => Left(InvalidRequest)
      case _ => Left(UnhandledStatus)
    }  
  }
  
}