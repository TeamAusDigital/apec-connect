package au.com.agiledigital.common.healthchecker

import au.com.agiledigital.healthchecker._
import play.api.Configuration

import scala.concurrent.{ ExecutionContext, Future }

/**
  * Creates instances of the [[VersionHealthChecker]].
  */
class VersionHealthCheckerFactory extends HealthCheckerFactory {
  override def createHealthChecker(checkerConfiguration: Configuration): VersionHealthChecker =
    new VersionHealthChecker(checkerConfiguration)
}

/**
  * Health checker that displays the application build and version information.
  * as set in application.conf
  *
  * "environment"  and "build.release" are presently used
  *
  * @param configuration the configuration for this checker. environment and build.release strings are extracted
  */
class VersionHealthChecker(override val configuration: Configuration)
    extends BaseHealthChecker with ConfigurationBasedHealthChecker {

  private val environment = configuration.getString("name").getOrElse("Environment Unknown")
  private val release = configuration.getString("release").getOrElse("Release Unknown")

  override val name = "Version Health Checker"

  override def doCheck()(implicit ec: ExecutionContext): Future[HealthCheckOutcome] =
    Future.successful(HealthCheckOutcome(
      HealthCheckStatus.Ok,
      Some(name),
      Some(s"[$release] in [$environment]"),
      None
    ))
}
