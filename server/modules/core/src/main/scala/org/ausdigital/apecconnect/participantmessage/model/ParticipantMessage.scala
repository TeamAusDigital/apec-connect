package org.ausdigital.apecconnect.participantmessage.model

import org.ausdigital.apecconnect.db.model.{Record, RecordId}
import org.ausdigital.apecconnect.invoice.model.Invoice.{Invoice, InvoiceId}
import org.ausdigital.apecconnect.participants.model.Participant.Participant
import play.api.libs.json.{Json, OFormat}

/**
  * Message that sends across Participants.
  * The message may contain an associated Invoice to indicate that a receiver needs to make a payment.
  */
object ParticipantMessage {

  final case class ParticipantMessageData(
      senderId: String,
      receiverId: String,
      message: Option[String],
      rating: Option[Int],
      invoiceId: Option[InvoiceId]
  )

  final case class ParticipantMessageDetails(
      sender: Participant,
      receiver: Participant,
      message: ParticipantMessage,
      invoice: Option[Invoice]
  )

  final case class PendingParticipantMessage(receiverId: String, message: Option[String], rating: Option[Int], invoiceId: Option[InvoiceId])

  type ParticipantMessage = Record[ParticipantMessageData]

  type ParticipantMessageId = RecordId[ParticipantMessage]

  def messageTransportToMessageData(senderId: String, pending: PendingParticipantMessage) =
    ParticipantMessageData(
      senderId = senderId,
      receiverId = pending.receiverId,
      message = pending.message,
      rating = pending.rating,
      invoiceId = pending.invoiceId
    )

  implicit val messageFormatter: OFormat[ParticipantMessageData] = Json.format[ParticipantMessageData]

  implicit val messageToSendTransportFormatter: OFormat[PendingParticipantMessage] = Json.format[PendingParticipantMessage]

  implicit val messageWithInvoiceFormatter: OFormat[ParticipantMessageDetails] = Json.format[ParticipantMessageDetails]
}
