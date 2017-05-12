package org.ausdigital.apecconnect.message.model

import org.ausdigital.apecconnect.db.model.{Record, RecordId}
import org.ausdigital.apecconnect.invoice.model.Invoice.Invoice
import play.api.libs.json.{Json, OFormat}


object ParticipantMessage {

  final case class ParticipantMessageData(
    senderId: String,
    receiverId: String,
    message: Option[String],
    rating: Option[Int],
    invoice: Option[Invoice]
  )

  type ParticipantMessage = Record[ParticipantMessageData]

  type ParticipantMessageId = RecordId[ParticipantMessage]

  implicit val format: OFormat[ParticipantMessageData] = Json.format[ParticipantMessageData]
}
