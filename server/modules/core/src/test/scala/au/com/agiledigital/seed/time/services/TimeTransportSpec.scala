package au.com.agiledigital.seed.time.services

import au.com.agiledigital.rest.tests.BaseSpec
import au.com.agiledigital.seed.time.config.TimeTransportConfig
import mockws.{ MockWS, Route }
import org.specs2.concurrent.ExecutionEnv
import org.specs2.specification.Scope
import play.api.mvc.Action
import play.api.mvc.Results._
import play.api.test.Helpers._

/**
  * Contains unit tests for the [[TimeTransport]].
  */
class TimeTransportSpec(implicit ev: ExecutionEnv) extends BaseSpec {

  "Fetching the time" should {
    "return the time from the remote system, in millis" in new WithSystemTimeTransport {
      // Given a system time transport

      // And a remote system that responds with the time in seconds.
      override val timeRoute = Route {
        case (GET, `baseUrl`) => Action { request =>
          request.getQueryString("""\s""") aka "The expected time format query string" must_=== Some("")
          Ok("1001010")
        }
      }

      // When the remote time is requested.
      val actual = transport.fetchTime()

      // Then it should match the expected time in millis.
      actual must beEqualTo(1001010000).awaitFor(defaultAwait)
    }
    "fail if the remote system responds with invalid data" in new WithSystemTimeTransport {
      // Given a system time transport

      // And a remote system that responds with randomness
      override val timeRoute = Route {
        case (GET, `baseUrl`) => Action { request =>
          Ok("randomness")
        }
      }

      // When the remote time is requested.
      val actual = transport.fetchTime()

      // Then it should have failed.
      actual must throwAn[NumberFormatException].awaitFor(defaultAwait)
    }
  }

  /**
    * Provides test specific fixtures.
    */
  trait WithSystemTimeTransport extends Scope {

    lazy val baseUrl = "test_url"

    lazy val config = new TimeTransportConfig(baseUrl)

    /**
      * The route that tests may override to setup their tests.
      */
    def timeRoute: Route

    lazy val mockWs = MockWS(timeRoute)

    lazy val transport = new TimeTransport(mockWs, config)
  }

}
