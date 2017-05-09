package au.com.agiledigital.seed.time.controllers

import au.com.agiledigital.rest.controllers.transport.{ JsonApiResponse, Message, MessageLevel }
import au.com.agiledigital.rest.tests.{ BaseSpec, NoStackTraceException }
import au.com.agiledigital.seed.time.model.{ Time, TimeDataProvider }
import au.com.agiledigital.seed.time.services.TimeService
import org.specs2.concurrent.ExecutionEnv
import org.specs2.specification.Scope
import play.api.mvc.{ ActionBuilder, Request, Result }
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.{ Await, ExecutionContext, Future }

/**
  * Contains Application-less unit tests for the [[TimeActions]].
  */
class TimeActionsSpec(implicit ev: ExecutionEnv) extends BaseSpec {

  "Requesting the system time" should {
    "return the time returned by the service" in new WithSystemActions {
      // Given a service that returns a known time.
      val providedTime = TimeDataProvider.time
      service.systemTime() returns providedTime

      // When the time action is invoked.
      val result = actions.time.apply(FakeRequest())

      // Then a 200 should have been returned.
      status(result) must_=== OK

      // And it should contain the system time.
      val json = contentAsJson(result)
      val jsonApiResponse = json.as[JsonApiResponse[Time]]

      jsonApiResponse must_===
        JsonApiResponse(Some(providedTime), Message("Current time is [system date time].", MessageLevel.Info))
    }

    "fail if the service fails" in new WithSystemActions {
      // Given a service that explodes.
      service.systemTime() throws new NoStackTraceException("service failure")

      // When the time action in invoked.
      actions.time.apply(FakeRequest()) must throwA[NoStackTraceException]
    }
  }

  "Requesting the remote system time" should {
    "return the time returned by the service" in new WithSystemActions {
      // Given a service that returns a known remote time.
      val providedTime = TimeDataProvider.time
      service.remoteTime() returns Future.successful(providedTime)

      // When the time action is invoked.
      val result = actions.remoteTime.apply(FakeRequest())

      // Then a 200 should have been returned.
      status(result) must_=== OK

      // And it should contain the system time.
      val json = contentAsJson(result)
      val jsonApiResponse = json.as[JsonApiResponse[Time]]

      jsonApiResponse must_===
        JsonApiResponse(Some(providedTime), Message("Remote time is [system date time].", MessageLevel.Info))
    }
    "fail with contextual information if the service fails" in new WithSystemActions {
      // Given a service that fails.
      val thrownException = new NoStackTraceException("remote time failure")
      service.remoteTime() returns Future.failed(thrownException)

      // When the time action is invoked.
      val request = FakeRequest()
      val result = actions.remoteTime.apply(request)

      // Then a failing future should be returned.
      Await.result(result, defaultAwait) must throwA[RuntimeException](message = "Remote time service failed.")
    }
  }

  /**
    * Provides fixtures particular to this test.
    */
  trait WithSystemActions extends Scope {

    lazy val service: TimeService = mock[TimeService]

    lazy val actions: TimeActions = new TimeActions {
      override def ec: ExecutionContext = ev.ec
      override protected def timeService: TimeService = service
      override protected def timeAction(): ActionBuilder[TimeRequest] = new ActionBuilder[TimeRequest] {
        override def invokeBlock[A](request: Request[A], block: (TimeRequest[A]) => Future[Result]): Future[Result] = {
          block(TimeRequest(request))
        }
      }
    }
  }

}
