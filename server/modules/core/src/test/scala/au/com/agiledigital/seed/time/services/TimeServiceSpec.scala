package au.com.agiledigital.seed.time.services

import java.time.{ Clock, Instant, ZoneOffset }
import au.com.agiledigital.rest.tests.{ NoStackTraceException, BaseSpec }
import au.com.agiledigital.seed.time.model.Time
import org.specs2.concurrent.ExecutionEnv
import org.specs2.specification.Scope

import scala.concurrent.{ Future, ExecutionContext }

/**
  * Contains unit tests for the [[TimeService]].
  */
class TimeServiceSpec(implicit ev: ExecutionEnv) extends BaseSpec {

  "Fetching the system time" should {
    "return the current time" in new WithSystemTimeService {
      // Given a system time service

      // That thinks it is a particular time.
      override val time = 10012245L

      // When the current time is requested.
      val actual = service.systemTime()

      // Then it should match the expected time.
      actual must_=== Time(
        "1970-01-01",
        "1970-01-01T02:46:52.245",
        time
      )
    }
  }

  "Fetching the remote time" should {
    "return the time returned by the transport" in new WithSystemTimeService {
      // Given a system time service

      // That thinks it is a particular time.
      override def time: Long = 1253885L

      // When the remote time is requested.
      val actualFuture = service.remoteTime()

      // Then it should have returned a Future that will provide the expected time.
      actualFuture must beEqualTo(Time(
        "1970-01-01",
        "1970-01-01T00:20:53.885",
        time
      )).awaitFor(defaultAwait)
    }
    "fail is the transport fails" in new WithSystemTimeService {
      // Given a system time service

      // That thinks it is a particular time.
      override def time: Long = 1253885L

      // And a transport that fails.
      transport.fetchTime()(any[ExecutionContext]) returns Future.failed(new NoStackTraceException("failed transport"))

      // Then when the time is fetched.
      val actualFuture = service.remoteTime()

      // Then a failed future should have been returned.
    }
  }

  /**
    * Provides test specific fixtures.
    */
  trait WithSystemTimeService extends Scope {

    def time: Long

    lazy val clock: Clock = Clock.fixed(Instant.ofEpochMilli(time), ZoneOffset.UTC)

    lazy val transport = {
      val t = mock[TimeTransport]
      t.fetchTime()(any[ExecutionContext]) returns Future.successful(time)
      t
    }

    lazy val service = new TimeService(au.com.agiledigital.kamon.play_extensions.test.metrics, clock, transport)
  }

}
