package org.ausdigital.apecconnect.participantmessage.model

import org.ausdigital.apecconnect.db.model.{Record, RecordId}
import org.ausdigital.apecconnect.invoice.model.Invoice.{Invoice, InvoiceId}
import play.api.libs.json.{Json, OFormat}

object ParticipantMessage {

  final case class ParticipantMessageData(
      senderId: String,
      receiverId: String,
      message: Option[String],
      rating: Option[Int],
      invoiceId: Option[InvoiceId]
  )

  final case class ParticipantMessageWithInvoice(
      id: ParticipantMessageId,
      message: ParticipantMessage,
      invoice: Invoice
  )

  final case class ParticipantMessageTransport(
      message: ParticipantMessageData,
      invoice: Option[Invoice]
  )

  type ParticipantMessage = Record[ParticipantMessageData]

  type ParticipantMessageId = RecordId[ParticipantMessage]


  implicit val messageFormatter: OFormat[ParticipantMessageData] = Json.format[ParticipantMessageData]

  implicit val messageTransportFormatter: OFormat[ParticipantMessageTransport] = Json.format[ParticipantMessageTransport]

  implicit val messageWithInvoiceFormatter: OFormat[ParticipantMessageWithInvoice] = Json.format[ParticipantMessageWithInvoice]
}
