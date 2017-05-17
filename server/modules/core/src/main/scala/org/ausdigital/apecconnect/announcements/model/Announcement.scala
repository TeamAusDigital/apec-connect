package org.ausdigital.apecconnect.announcements.model

import org.ausdigital.apecconnect.db.model.{Record, RecordId}
import org.ausdigital.apecconnect.invoice.model.Invoice.{Invoice, InvoiceId}
import org.ausdigital.apecconnect.participants.model.Participant.Participant
import play.api.libs.json.{Json, OFormat}

/**
  * Broadcase message that sends to all Participants.
  */
object Announcement {

  final case class AnnouncementData(
      senderId: String,
      message: String
  )

  type Announcement = Record[AnnouncementData]

  type AnnouncementId = RecordId[Announcement]

  implicit val announcementFormatter: OFormat[AnnouncementData] = Json.format[AnnouncementData]

}
