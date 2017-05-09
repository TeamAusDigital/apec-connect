package au.com.agiledigital.seed.time.controllers

import javax.inject.Inject

import au.com.agiledigital.seed.time.services.TimeService
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

/**
  * Takes the TimeActions and combines them with the requirements for calling them (in this case a builder that
  * constructs a SystemRequest from the supplied request and the service).
  */
class TimeController @Inject() (override val timeService: TimeService)(implicit val ec: ExecutionContext) extends Controller with TimeActions {

  /**
    * Action chain should be defined as an inner class as some request types, e.g SecuredRequest are path-dependent.
    * @return the action builder.
    */
  override protected def timeAction(): ActionBuilder[TimeRequest] = new ActionBuilder[TimeRequest] {
    override def invokeBlock[A](request: Request[A], block: (TimeRequest[A]) => Future[Result]): Future[Result] = {
      val timeRequest = new TimeRequest[A](request)
      block(timeRequest)
    }
  }
}
