package au.com.agiledigital.seed.time.modules

import java.time.Clock

import au.com.agiledigital.common.tests.BaseApplications
import au.com.agiledigital.rest.tests.{ WithApplicationBuilder, BaseSpec }

/**
  * Contains unit tests for the [[TimeModule]].
  */
class TimeModuleSpec extends BaseSpec {

  def applicationBuilder = BaseApplications.appBuilder.overrides(new TimeModule)

  "Enabling the system module" should {
    "bind the clock" in new WithApplicationBuilder(applicationBuilder) {
      // Given an application that has the SystemModule enabled.

      // When the clock is injected.
      val clock = app.injector.instanceOf[Clock]

      // Then it should be set.
      clock must beEqualTo(Clock.systemDefaultZone())
    }
  }

}
