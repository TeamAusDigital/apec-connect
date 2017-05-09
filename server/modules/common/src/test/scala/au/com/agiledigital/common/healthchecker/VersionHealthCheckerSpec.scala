package au.com.agiledigital.common.healthchecker

import au.com.agiledigital.healthchecker.HealthCheckStatus
import au.com.agiledigital.rest.tests.BaseSpec
import org.specs2.concurrent.ExecutionEnv
import org.specs2.matcher.DataTables
import play.api.Configuration

import scala.concurrent.Await

/**
  * Tests for [[VersionHealthChecker]].
  */
class VersionHealthCheckerSpec(implicit ev: ExecutionEnv) extends BaseSpec with DataTables {

  "VersionHealthChecker" in {
    // format: OFF
      "environment"       || "release"    || "expected message_matcher" |>
      None                !! None         !! contain("[Environment Unknown]") |
      None                !! None         !! contain("[Release Unknown]") |
      None                !! Some("V1")   !! contain("[Environment Unknown]") |
      None                !! Some("V1")   !! contain("[V1]") |
      Some("uat")         !! Some("V2")   !! contain("[V2]") |
      Some("uat")         !! Some("V2")   !! contain("[uat]") |
      Some("uat")         !! Some("V2")   !! not(contain("Unknown")) |
      Some("staging")     !! None         !! contain("[Release Unknown]") |
      Some("staging")     !! None         !! contain("[staging]") |> {
        // format: ON
        (environment, release, expectedMessage) =>
          {

            val configuration = mock[Configuration]
            configuration.getString(===("release"), any[Option[Set[String]]]) returns release
            configuration.getString(===("name"), any[Option[Set[String]]]) returns environment

            val checker = new VersionHealthChecker(configuration)

            val result = Await.result(checker.doCheck(), defaultAwait)
            result.status must_== HealthCheckStatus.Ok
            result.message aka s"Result message [${result.message}] should incorporate [$environment] and [$release]." must beSome(expectedMessage)
          }
      }
  }
}
