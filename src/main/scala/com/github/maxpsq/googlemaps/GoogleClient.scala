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
  
  private val overQuotaMax = 10
  private var overQuotaCounter = 0
  
  def resetOverQuota: Unit = overQuotaCounter = 0
  def incOverQuota: Unit = {
    overQuotaCounter += 1
    if ( overQuotaCounter > overQuotaMax ) throw 
          new RuntimeException(s"Maximum (${overQuotaCounter}) OverQuotaLimit exceeded")
  }
  
  
  /** 
    * Creates a collection of tupled parameters
    * from a given set of Parameters and checking 
    * for the correctness.
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
  def reqHandler[T](req: Req)
                   (implicit ec: ExecutionContext, statusReads: Reads[StatusResponse], dataReads: Reads[T])
                   : Future[Either[Error, T]] = {
    
    def statusExtractor(json: JsValue) = readsEither[StatusResponse](json)
    def dataExtractor(json: JsValue) = readsEither[T](json)
    
    http(req OK as.String).fold ( 
        th => { Left(HttpError(th.getMessage())) },
        str => { val json = Json.parse(str)
          statusExtractor(json).right.flatMap{ sr => 
            evalStatus(sr).right.flatMap{ ok =>
              dataExtractor(json)  
            }
          }
        }
    )  
  }
  
  
  def stopOnOverQuotaLimit[Z](block: => Z) = {
    block
  }
  
  /**
   * Validates the parameters
   */
  def validateParameters(pars: Seq[Parameter])(implicit ec: ExecutionContext): Future[Either[Error, Seq[Parameter]]] = Future {
    
    def validateParameter(par: Parameter): Either[Error,Parameter] = {
      par.isValid match {
        case true => Right(par)
        case false => Left(InvalidParameterError(par))
      }
    }
    
    def go(checked: Either[Error,Parameter], toBeChecked: Seq[Parameter], pars: Seq[Parameter]): Either[Error, Seq[Parameter]] = checked match {
      case Left(error) => Left(error)
      case Right(par) => toBeChecked match {
             case (Nil) => Right(pars)
             case (head :: tail) => go(validateParameter(head), tail, pars)
      }
    }

    go( validateParameter(pars.head), pars.tail, pars)
  }
  
  /** 
    * Validates the parsed JSON. 
    * Returns an Error or the data.
    * 
    * @param json the parsed JSON object from Google WS  
    * @param reads implicit deserializer
    */
  private def readsEither[Z](json: JsValue)(implicit rds: Reads[Z]): Either[Error, Z] = { 
    rds.reads(json).map(Right(_)).recoverTotal { e => 
      Left(JsonParsingError(JsError.toFlatForm(e).toString))
    }
  }
  
  /**
   * Converts a ResponseStatus into a specific Error
   */
  private def evalStatus(statusResp: StatusResponse): Either[Error, _] = {
    val ( status, msg ) = statusResp match {
      case StatusResponse( status , Some(msg) ) => ( status,  msg)
      case StatusResponse( status , None ) =>  ( status,  status.toString())
    } 
    status match {
      case ResponseStatus.Ok => resetOverQuota ; Right("Ok")
      case ResponseStatus.ZeroResults => Left(ZeroResults(msg))
      case ResponseStatus.OverQueryLimit => incOverQuota ; Left(OverQuotaLimit(msg))
      case ResponseStatus.RequestDenied => Left(Denied(msg))
      case ResponseStatus.InvalidRequest => Left(InvalidRequest(msg))
      case _  => Left(UnhandledStatus(msg))
    }  
  }
  
}