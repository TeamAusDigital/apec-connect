package org.ausdigital.apecconnect.businessregister

import javax.inject.Inject

import org.ausdigital.apecconnect.businessregister.model.{ ApecConnectBusinessRegisterException, ParticipantRegistrationPayload, ParticipantRegistrationResponse }
import org.ausdigital.apecconnect.common.model.ApecConnectBusinessRegisterConfig
import play.api.{ Configuration, Logger }
import play.api.http.Status
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import scala.concurrent.{ ExecutionContext, Future }
import scalaz._
import Scalaz._
import scala.util.control.NonFatal
import scala.util.Random.nextInt

class ApecConnectBusinessRegister @Inject() (ws: WSClient, configuration: Configuration) {

  val apecBusinessRegisterConfig: ApecConnectBusinessRegisterConfig = configuration.underlying.as[ApecConnectBusinessRegisterConfig]("apec-connect-business-register")

  /**
   * Sign up the participant with APEC Connect Business Register.
   * TODO: error handling based on the API response.
   * @see https://github.com/TeamAusDigital/apec-connect-cloud-ledger/blob/master/business-register/API.md
   * @param participantRegistrationPayload payload required to register a participant.
   * @return either a registered participant or error message if registration process failed.
   */
  def signUp(participantRegistrationPayload: ParticipantRegistrationPayload)(implicit ec: ExecutionContext): Future[\/[String, ParticipantRegistrationResponse]] = {

    val username = participantRegistrationPayload.businessName.replaceAll("\\W*", "").toLowerCase.take(4) + "-" + haiku

    ws.url(apecBusinessRegisterConfig.createUserApi).post(Json.toJson(participantRegistrationPayload.copy(username = Some(username)))).map { response =>

      Logger.warn(s"We have a response ${response.body}")

      if (response.status === Status.CREATED || response.status === Status.OK) {
        try {
          response.json
            .validate[ParticipantRegistrationResponse]
            .fold(
              error => {
                s"Invalid response returned from APEC Connect Business Register - [$error].".left
              },
              participantRegistrationResponse => {
                participantRegistrationResponse.right
              }
            )
        } catch {
          case NonFatal(ex) => throw new ApecConnectBusinessRegisterException(s"Failed to sign up with APEC Connect Business Register.", ex)
        }
      } else {
        s"Failed to register this participant with APEC Connect Business Register. Got response code [${response.status}] - [${response.body}]".left
      }
    }
  }

  // Reference: https://kernelgarden.wordpress.com/2014/06/27/a-heroku-like-name-generator-in-scala/
  val adjs = List("autumn", "hidden", "bitter", "misty", "silent",
    "reckless", "daunting", "short", "rising", "strong", "timber", "tumbling",
    "silver", "dusty", "celestial", "cosmic", "crescent", "double", "far",
    "terrestrial", "huge", "deep", "epic", "titanic", "mighty", "powerful")

  val nouns = List("waterfall", "river", "breeze", "moon", "rain",
    "wind", "sea", "morning", "snow", "lake", "sunset", "pine", "shadow", "leaf",
    "sequoia", "cedar", "wrath", "blessing", "spirit", "nova", "storm", "burst",
    "giant", "elemental", "throne", "game", "weed", "stone", "apogee", "bang")

  def getRandElt[A](xs: List[A]): A = xs.apply(nextInt(xs.size))

  def getRandNumber(ra: Range): String = {
    (ra.head + nextInt(ra.end - ra.head)).toString
  }

  def haiku: String = {
    val xs = getRandNumber(1000 to 9999) :: List(nouns, adjs).map(getRandElt)
    xs.reverse.mkString("-")
  }
}
