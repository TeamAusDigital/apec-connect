package org.ausdigital.apecconnect.participants.controllers

import javax.inject.Inject

import org.ausdigital.apecconnect.nelAsResponse
import au.com.agiledigital.rest.controllers.transport.JsonApiResponse
import au.com.agiledigital.rest.security.BodyParsers
import io.kanaka.monadic.dsl._
import io.kanaka.monadic.dsl.compat.scalaz._
import org.ausdigital.apecconnect.admin.controllers.AuthorisingController
import org.ausdigital.apecconnect.auth.{ApecConnectSilhouette, ParticipantTokenService}
import org.ausdigital.apecconnect.businessregister.ApecConnectBusinessRegister
import org.ausdigital.apecconnect.businessregister.model.ParticipantRegistrationPayload
import org.ausdigital.apecconnect.common.random.RandomWordsGenerator
import org.ausdigital.apecconnect.invoice.services.InvoiceService
import org.ausdigital.apecconnect.participantmessage.model.ParticipantMessage.ParticipantMessageTransport
import org.ausdigital.apecconnect.participantmessage.services.ParticipantMessageService
import org.ausdigital.apecconnect.participants.model.Participant.ParticipantData
import org.ausdigital.apecconnect.participants.services.ParticipantService
import org.ausdigital.apecconnect.db.model.RecordOps._
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.ExecutionContext

/**
  * Provides public APIs related to Participant of APEC Connect application.
  */
class ParticipantsController @Inject()(
    apecConnectBusinessRegister: ApecConnectBusinessRegister,
    participantService: ParticipantService,
    participantTokenService: ParticipantTokenService,
    invoiceService: InvoiceService,
    participantMessageService: ParticipantMessageService,
    override val apecConnectSilhouette: ApecConnectSilhouette
)(override implicit val executionContext: ExecutionContext)
    extends AuthorisingController
    with Controller {

  /**
    * Registers the participant with APEC Connect Business Register.
    * @return 200 OK with generated participant token (JWT), otherwise return 400 bad request if failed to register the
    *         participant with APEC Connect Business Register, or 500 if authentication token generation failed internally.
    */
  def signUp(): Action[ParticipantRegistrationPayload] = Action.async(BodyParsers.whitelistingJson[ParticipantRegistrationPayload]) { implicit request =>
    val participantRegistrationPayload = request.body

    for {
      // TODO: handles the validation errors from business register, e.g. using a failureNel.
      registeredParticipant <- apecConnectBusinessRegister.signUp(participantRegistrationPayload.copy(username = Some(generateUsername(participantRegistrationPayload.businessName)))) ?| { error =>
        JsonApiResponse.badRequestResponse(error, Nil)
      }
      participant <- participantService.create(
        ParticipantData(
          identifier = registeredParticipant.uuid,
          authToken = registeredParticipant.access_token,
          businessName = request.body.businessName,
          email = request.body.email,
          username = registeredParticipant.username,
          phone = request.body.phone
        )
      ) ?| nelAsResponse
      participantToken <- participantTokenService.generateParticipantToken(participant.identifier) ?| JsonApiResponse.internalServerErrorResponse(
        "Failed to generate auth token for registered participant."
      )
    } yield JsonApiResponse.buildResponse("Successfully signed up the participant.", participantToken)
  }

  def lookupParticipants(query: String): Action[AnyContent] = Action.async { implicit request =>
    for {
      participantsResults <- apecConnectBusinessRegister.lookupParticipants(query) ?| JsonApiResponse.internalServerErrorResponse(s"Failed to look up participants with query [$query].")
    } yield JsonApiResponse.buildResponse("Successfully found participants.", participantsResults)
  }

  def participant(): Action[AnyContent] = participantAction.async { implicit request =>
    for {
      participant <- participantService.findById(request.identity.id)
    } yield JsonApiResponse.buildResponse(s"Successfully found participant.", participant)
  }

  def createParticipantMessage(): Action[ParticipantMessageTransport] = participantAction.async(BodyParsers.whitelistingJson[ParticipantMessageTransport]) { implicit request =>
    for {
      participant <- participantService.findById(request.identity.id)                                               ?| JsonApiResponse.notFoundResponse("Cannot find sender participant to create message.")
      message     <- participantMessageService.create(request.body.message.copy(senderId = participant.identifier)) ?| nelAsResponse
    } yield JsonApiResponse.buildResponse(s"Successfully created message [${message.id}]", message)
  }

  def participantMessages(): Action[AnyContent] = participantAction.async { implicit request =>
    for {
      participant <- participantService.findById(request.identity.id) ?| JsonApiResponse.notFoundResponse("Cannot find participant.")
      messages <- participantMessageService.queryParticipantMessages(participant) ?| JsonApiResponse.internalServerErrorResponse("Failed to find participant messages.")
    } yield JsonApiResponse.buildResponse(s"Found messages for participant [${request.identity.id}].", messages)
  }

  /**
    * Generating random username based on provided business name.
    * TODO: Make sure the username will be unique after generated.
    * @param businessName that is used as base of the username.
    * @return generated random username.
    */
  private def generateUsername(businessName: String): String = {
    val shortBusinessName = businessName.replaceAll("\\W*", "").toLowerCase.take(4)
    shortBusinessName + RandomWordsGenerator.randomWords(!shortBusinessName.isEmpty, "-")
  }
}
