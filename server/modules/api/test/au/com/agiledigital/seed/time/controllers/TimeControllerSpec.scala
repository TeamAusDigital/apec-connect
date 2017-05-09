package au.com.agiledigital.seed.time.controllers

import au.com.agiledigital.rest.tests.BaseSpec
import au.com.agiledigital.seed.time.services.TimeService
import org.specs2.concurrent.ExecutionEnv
import org.specs2.specification.Scope
import play.api.http.HttpErrorHandler
import play.api.mvc.ActionBuilder
import play.api.test.FakeRequest

import scala.language.reflectiveCalls

/**
  * Contains tests for the [[TimeController]].
  */
class TimeControllerSpec(implicit ev: ExecutionEnv) extends BaseSpec {

  "Invoking the time" should {
    "wrap the request as expected" in new WithSystemController {
      // Given a request.
      val fakeRequest = FakeRequest()

      // When it is passed into a system action.
      val extraActionController = new TimeController(service) {
        def doAction() = timeAction() { request =>
          // Then it should have been wrapped in a system request.
          request.request must_=== fakeRequest
          Ok
        }
      }

      extraActionController.doAction().apply(fakeRequest)
    }
  }

  /**
    * Provides fixtures particular to this test.
    */
  trait WithSystemController extends Scope {
    lazy val service: TimeService = mock[TimeService]
    lazy val controller: TimeController = new TimeController(service) {
      override def timeAction(): ActionBuilder[TimeRequest] = super.timeAction()
    }
  }

}

