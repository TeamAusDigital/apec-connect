package au.com.agiledigital.seed.test

import au.com.agiledigital.common.tests.BaseApplications
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import org.specs2.mock.mockito.MocksCreation
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.routing.Router

/**
  * Provides the base applications that can be used for testing.
  */
object Applications extends MocksCreation {

  /**
    * Builds the base application with the majority of modules disabled.
    */
  private def baseAppBuilder = BaseApplications.appBuilder

  /**
    * A Guice application builder that provides the minimum possible and disables routing to stop
    * controllers being instantiated and dragging in many dependencies.
    */
  def publicGuiceAppBuilder: GuiceApplicationBuilder = baseAppBuilder.
    overrides(new FakeRouterModule())

  /**
    * A Guice application builder that provides the injectables required to instantiate controllers.
    */
  def publicRoutableGuiceAppBuilder(): GuiceApplicationBuilder =
    baseAppBuilder.overrides(new FakeModule())

}

/**
  * A fake Guice module that provides the injectables required by the controllers.
  */
class FakeModule() extends AbstractModule with ScalaModule {
  def configure() = {
    // Provide mocked injectables here.
  }
}

/**
  * A fake Guice module that disables controllers creation by supplying an empty router.
  */
class FakeRouterModule extends AbstractModule with ScalaModule {
  def configure() = {
    bind[Router].toInstance(Router.empty)
  }
}