package org.ausdigital.apecconnect.businessregister

import javax.inject.Inject

import org.ausdigital.apecconnect.businessregister.model.{ApecConnectBusinessRegisterException, LookupParticipantsResponse, ParticipantRegistrationPayload, ParticipantRegistrationResponse}
import org.ausdigital.apecconnect.common.model.ApecConnectBusinessRegisterConfig
import play.api.{Configuration, Logger}
import play.api.http.Status
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

import scala.concurrent.{ExecutionContext, Future}
import scalaz._
import Scalaz._
import scala.util.control.NonFatal

/**
  * Provides APEC Connect Business Register API integrations.
  */
class ApecConnectBusinessRegister @Inject() (ws: WSClient, configuration: Configuration) {

  val apecBusinessRegisterConfig: ApecConnectBusinessRegisterConfig = configuration.underlying.as[ApecConnectBusinessRegisterConfig]("apec-connect-business-register")

  /**
   * Sign up the Participant with APEC Connect Business Register.
   * TODO: error handling based on the API response.
   * @see https://github.com/TeamAusDigital/apec-connect-cloud-ledger/blob/master/business-register/API.md
   * @param participantRegistrationPayload payload required to register a participant.
   * @return either a registered participant or error message if registration process failed.
   */
  def signUp(participantRegistrationPayload: ParticipantRegistrationPayload)(implicit ec: ExecutionContext): Future[\/[String, ParticipantRegistrationResponse]] =
    ws.url(apecBusinessRegisterConfig.createUserApi).post(Json.toJson(participantRegistrationPayload)).map { response =>
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
        (response.json \ "email").head.asOpt[String].
          orElse((response.json \ "phone").head.asOpt[String]).
          getOrElse("Failed to sign up with APEC Connect Business Register.").left
      }
    }

  /**
    * Looks up the Participants on APEC Connect Business Register.
    * @param query used to search the APEC Connect Business Register for Participants.
    * @return either a lookup response or error message if failed.
    */
  def lookupParticipants(query: String)(implicit ec: ExecutionContext): Future[\/[String, LookupParticipantsResponse]] =
    ws.url(s"${apecBusinessRegisterConfig.businessLookupApi}?q=$query").get.map { response =>
      if (response.status === Status.OK) {
        try {
          response.json
            .validate[LookupParticipantsResponse]
            .fold(
              error => {
                s"Invalid response returned when looking up APEC Connect businesses - [$error].".left
              },
              lookupBusinessResponse => {
                lookupBusinessResponse.right
              }
            )
        } catch {
          case NonFatal(ex) => throw new ApecConnectBusinessRegisterException(s"Failed to look up business with query [$query] on APEC Connect Business Register.", ex)
        }
      } else {
        s"Failed to look up business with query [$query] on APEC Connect Business Register. Got response code [${response.status}] - [${response.body}]".left
      }
    }
}

