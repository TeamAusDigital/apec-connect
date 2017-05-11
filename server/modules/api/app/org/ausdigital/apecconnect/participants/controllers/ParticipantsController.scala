package org.ausdigital.apecconnect.participants.controllers

import javax.inject.Inject

import au.com.agiledigital.rest.controllers.transport.{ JsonApiResponse, Message, MessageLevel }
import au.com.agiledigital.rest.security.BodyParsers
import io.kanaka.monadic.dsl._
import io.kanaka.monadic.dsl.compat.scalaz._
import org.ausdigital.apecconnect.db.model.RecordOps._
import org.ausdigital.apecconnect.admin.controllers.AuthorisingController
import org.ausdigital.apecconnect.auth.{ ApecConnectSilhouette, ParticipantTokenService }
import org.ausdigital.apecconnect.businessregister.ApecConnectBusinessRegister
import org.ausdigital.apecconnect.businessregister.model.ParticipantRegistrationPayload
import org.ausdigital.apecconnect.participants.model.Participant.ParticipantData
import org.ausdigital.apecconnect.participants.services.ParticipantService
import play.api.libs.json.Json
import play.api.mvc.{ Action, AnyContent, Controller, Result }

import scala.concurrent.ExecutionContext
import scalaz.NonEmptyList

class ParticipantsController @Inject() (
  apecConnectBusinessRegister: ApecConnectBusinessRegister,
  participantService: ParticipantService,
  participantTokenService: ParticipantTokenService,
  override val apecConnectSilhouette: ApecConnectSilhouette
)(override implicit val executionContext: ExecutionContext)
    extends AuthorisingController with Controller {

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
      participantToken <- participantTokenService.generateParticipantToken(participant.identifier) ?| JsonApiResponse.internalServerErrorResponse("Failed to generate auth token for registered participant.")
    } yield JsonApiResponse.buildResponse("Successfully signed up the participant.", participantToken)
  }

  def participant(): Action[AnyContent] = participantAction.async { implicit request =>
    for {
      participant <- participantService.findById(request.identity.id)
    } yield JsonApiResponse.buildResponse(s"Successfully found participant.", participant)
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
