package au.com.agiledigital.common.tests

import akka.actor.ActorSystem
import au.com.agiledigital.healthchecker.{ HealthCheckManager, HealthCheckerModule }
import au.com.agiledigital.kamon.play_extensions.{ KamonPlayExtensionsModule, Metrics }
import kamon.play.di.KamonModule
import play.api.Mode
import play.api.inject._
import play.api.inject.guice.GuiceApplicationBuilder

/**
  * Contains reusable test fixtures.
  */
object BaseApplications {

  /**
    * A Guice application builder that disables modules that should never be enabled.
    */
  def appBuilder: GuiceApplicationBuilder = {
    new GuiceApplicationBuilder().
      in(Mode.Test).
      disable[HealthCheckerModule].
      disable[KamonModule].
      disable[KamonPlayExtensionsModule].
      overrides(bind[Metrics].to(au.com.agiledigital.kamon.play_extensions.test.metrics)).
      overrides(bind[HealthCheckManager].to(emptyHealthCheckManager)).
      configure("kamon.show-aspectj-missing-warning" -> false)
  }

  /**
    * A do nothing health check manager.
    */
  val emptyHealthCheckManager: HealthCheckManager =
    new HealthCheckManager(Nil, ActorSystem.create("no-op-health-checks"))
}
