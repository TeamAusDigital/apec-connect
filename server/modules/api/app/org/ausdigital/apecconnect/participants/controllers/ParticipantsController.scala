package org.ausdigital.apecconnect.participants.controllers

import javax.inject.Inject

import au.com.agiledigital.rest.controllers.transport.{ JsonApiResponse, Message, MessageLevel }
import au.com.agiledigital.rest.security.BodyParsers
import io.kanaka.monadic.dsl._
import io.kanaka.monadic.dsl.compat.scalaz._
import org.ausdigital.apecconnect.businessregister.ApecConnectBusinessRegister
import org.ausdigital.apecconnect.businessregister.model.ParticipantRegistrationPayload
import org.ausdigital.apecconnect.participants.model.Participant.ParticipantData
import org.ausdigital.apecconnect.participants.services.ParticipantService
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller, Result }

import scala.concurrent.ExecutionContext
import scalaz.NonEmptyList

class ParticipantsController @Inject() (apecConnectBusinessRegister: ApecConnectBusinessRegister, participantService: ParticipantService)(implicit val ec: ExecutionContext)
    extends Controller {

  def signUp(): Action[ParticipantRegistrationPayload] = Action.async(BodyParsers.whitelistingJson[ParticipantRegistrationPayload]) { implicit request =>
    for {
      // TODO: handles the validation errors from business register, e.g. using a failureNel.
      registeredParticipant <- apecConnectBusinessRegister.signUp(request.body) ?| { error =>
        JsonApiResponse.badRequestResponse(s"Failed to sign up participant - [$error].", Nil)
      }
      participant <- participantService.create(ParticipantData(
        identifier = registeredParticipant.uuid,
        authToken = registeredParticipant.access_token,
        businessName = request.body.businessName,
        email = request.body.email,
        phone = request.body.phone
      )) ?| nelAsResponse
    } yield JsonApiResponse.buildResponse("Successfully signed up the participant.", participant)
  }

  /**
   * Converts a non-empty list of errors into a Result.
   * TODO: pull this out as common share function.
   * @return the Result.
   */
  def nelAsResponse: (NonEmptyList[String]) => Result = errors => {
    val response = errors.tail.foldLeft(JsonApiResponse[String](result = None, message = Message(errors.head, MessageLevel.Alert))) {
      case (r, error) => r.withMessage(Message(error, MessageLevel.Alert))
    }
    BadRequest(Json.toJson(response))
  }
}
