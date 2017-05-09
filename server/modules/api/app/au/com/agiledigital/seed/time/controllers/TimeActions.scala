package au.com.agiledigital.seed.time.controllers

import au.com.agiledigital.rest.controllers.transport.JsonApiResponse
import au.com.agiledigital.seed.time.services.TimeService
import controllers.ActionDSL.MonadicActions
import play.api.mvc._

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

/**
  * Defines actions available on the system resource and the requirements for invoking those actions.
  */
trait TimeActions extends MonadicActions {

  implicit def ec: ExecutionContext

  /**
    * Provides time information.
    */
  protected def timeService: TimeService

  /**
    * Builder that accepts input (here nothing) and creates a Request specific to the actions. Used to define the
    * requirements for invoking the actions.
    *
    * Here the request is simply wrapped, but this builder may use an [[ActionRefiner]] to resolve the resource
    * being manipulated and provide that in the request.
    *
    * @return the action builder.
    */
  protected def timeAction(): ActionBuilder[TimeRequest]

  /**
    * Action that returns the current system time in millis and a formatted
    * date as a JSON object.
    */
  def time: Action[AnyContent] = timeAction() {
    val time = timeService.systemTime()
    JsonApiResponse.buildResponse(s"Current time is [${time.time}].", time)
  }

  /**
    * Action that returns the system time of a remote system.
    */
  def remoteTime: Action[AnyContent] = timeAction() async { request =>
    for {
      time <- timeService.remoteTime().recover {
        case NonFatal(e) =>
          // And some contextual information to the failure. If there was no useful contextual information to be added
          // it would be fine to let the failure propagate as is and be caught by the HttpErrorHandler.
          throw new RuntimeException("Remote time service failed.", e)
      }
    } yield JsonApiResponse.buildResponse(s"Remote time is [${time.time}].", time)
  }

  /**
    * Demo request that simply wraps another request.
    *
    * @param request the wrapped request.
    * @tparam A the type of the wrapped request.
    */
  case class TimeRequest[A](request: Request[A]) extends WrappedRequest[A](request)

}
