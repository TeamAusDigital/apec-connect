package au.com.agiledigital.seed.system.controllers

import javax.inject.Inject

import au.com.agiledigital.healthchecker.HealthCheckStatus.HealthCheckStatus
import au.com.agiledigital.healthchecker.{ HealthCheckManager, HealthCheckResult, HealthCheckStatus }
import au.com.agiledigital.rest.controllers.transport.{ JsonApiResponse, Message, MessageLevel }
import au.com.agiledigital.rest.json.EnumerationFormat
import au.com.agiledigital.seed.system.controllers.CheckHealthApiResponse._
import play.api.inject.Injector
import play.api.libs.json.{ Json, _ }
import play.api.mvc.{ AnyContent, Action }
import play.api.mvc.Results._

/**
  * Supports fetching the last known health of the system.
  */
class HealthCheckerController @Inject() (injector: Injector) {

  /**
    * Returns the last known health of the system. If the health is CRITICAL and serverErrorOnFailure is true, then
    * an InternalServerError will be returned.
    */
  def checkHealth(serverErrorOnFailure: Boolean): Action[AnyContent] = Action {

    val healthCheckerManager = injector.instanceOf[HealthCheckManager]

    val healthCheckResults: Seq[HealthCheckResult] = healthCheckerManager.health()

    // Determine overall status.
    val overallStatus = healthCheckerManager.overallStatus(healthCheckResults)

    val result = CheckHealthApiResponse(overallStatus, healthCheckResults)

    val message = s"Overall health is [$overallStatus]."

    // Return results and status as JSON.
    overallStatus match {
      case HealthCheckStatus.Error if serverErrorOnFailure =>
        InternalServerError(Json.toJson(JsonApiResponse(Some(result), Message(message, MessageLevel.Info))))
      case _ =>
        JsonApiResponse.buildResponse(s"Overall health is [$overallStatus].", Some(result))
    }
  }
}

/**
  * Response sent when the health of the system is requested.
  * @param overallStatus the overall status of the system.
  * @param healthCheckResults the individual statuses of the components in the system.
  */
final case class CheckHealthApiResponse(overallStatus: HealthCheckStatus, healthCheckResults: Seq[HealthCheckResult])

/**
  * Companion that contains the JSON formats and writes.
  */
object CheckHealthApiResponse {

  implicit val resultWrites: Writes[HealthCheckResult] = au.com.agiledigital.healthchecker.HealthCheckResult.HealthCheckResultWrites

  implicit val healthCheckStatusFormat: Format[HealthCheckStatus.Value] = EnumerationFormat.format(HealthCheckStatus)

  implicit val writes: Writes[CheckHealthApiResponse] = Json.writes[CheckHealthApiResponse]
}
