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
import org.ausdigital.apecconnect.participantmessage.model.ParticipantMessage.PendingParticipantMessage
import org.ausdigital.apecconnect.participantmessage.services.ParticipantMessageService
import org.ausdigital.apecconnect.participants.model.Participant.ParticipantData
import org.ausdigital.apecconnect.participants.services.ParticipantService
import org.ausdigital.apecconnect.db.model.RecordOps._
import org.ausdigital.apecconnect.participantmessage.model.ParticipantMessage
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
    * Registers the Participant with APEC Connect Business Register.
    * @return 200 OK with generated Participant token (JWT), otherwise return 400 bad request if failed to register the
    *         Participant with APEC Connect Business Register, or 500 if authentication token generation failed internally.
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
          businessName = participantRegistrationPayload.businessName,
          email = participantRegistrationPayload.email,
          economy = participantRegistrationPayload.economy,
          username = registeredParticipant.username,
          phone = request.body.phone
        )
      ) ?| nelAsResponse
      participantToken <- participantTokenService.generateParticipantToken(participant.identifier) ?| JsonApiResponse.internalServerErrorResponse(
        "Failed to generate auth token for registered Participant."
      )
    } yield JsonApiResponse.buildResponse("Successfully signed up the Participant.", participantToken)
  }

  /**
    * TODO: implement lookup through IBR, currently it only lookups records in APEC Connect system.
    * TODO: only supports query by business name for now.
    * Searches the Participant by business name.
    * @param query to search the Participants.
    * @return 200 OK with the Participants matches the query, or 500 internal error if failed to query.
    */
  def lookupParticipants(query: Option[String]): Action[AnyContent] = Action.async { implicit request =>
    for {
      participantsResults <- participantService.queryByBusinessName(query.getOrElse("")) ?| JsonApiResponse.internalServerErrorResponse(s"Failed to look up Participants with query [$query].")
    } yield JsonApiResponse.buildResponse("Successfully found Participants.", participantsResults)
  }

  /**
    * Finds the current authenticated Participant details.
    * @return 200 OK with the found Participant, or 404 not found if Participant not exist.
    */
  def participant(): Action[AnyContent] = participantAction.async { implicit request =>
    for {
      participant <- participantService.findById(request.identity.id) ?| JsonApiResponse.notFoundResponse(s"Cannot find the Participant with ID [${request.identity.id}].")
    } yield JsonApiResponse.buildResponse(s"Successfully found Participant.", participant)
  }

  /**
    * Creates a Participant message, with current authentiacted Participant as sender.
    * @return 200 OK with created message, or 404 if not Participant found, or 400 bad request if message creation failed.
    */
  def createParticipantMessage(): Action[PendingParticipantMessage] = participantAction.async(BodyParsers.whitelistingJson[PendingParticipantMessage]) { implicit request =>
    for {
      participant <- participantService.findById(request.identity.id)                                               ?| JsonApiResponse.notFoundResponse("Cannot find sender Participant to create message.")
      message     <- participantMessageService.create(ParticipantMessage.messageTransportToMessageData(participant.identifier, request.body)) ?| nelAsResponse
    } yield JsonApiResponse.buildResponse(s"Successfully created message [${message.id}]", message)
  }

  /**
    * Queries all messages related to authenticated Participant, Participant as sender or receiver for the message will be returned.
    * @return 200 OK with queried messages, or 500 internal error if failed to query the messages.
    */
  def participantMessages(): Action[AnyContent] = participantAction.async { implicit request =>
    for {
      participant <- participantService.findById(request.identity.id) ?| JsonApiResponse.notFoundResponse("Cannot find Participant.")
      messages <- participantMessageService.queryParticipantMessages(participant) ?| JsonApiResponse.internalServerErrorResponse("Failed to find Participant messages.")
    } yield JsonApiResponse.buildResponse(s"Found messages for Participant [${request.identity.id}].", messages)
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
