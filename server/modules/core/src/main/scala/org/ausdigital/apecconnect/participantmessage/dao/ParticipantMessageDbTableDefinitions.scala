package org.ausdigital.apecconnect.participantmessage.dao

import org.ausdigital.apecconnect.db.dao.BaseDbTableDefinitions
import org.ausdigital.apecconnect.invoice.model.Invoice.InvoiceId
import org.ausdigital.apecconnect.participantmessage.model.ParticipantMessage.{ParticipantMessage, ParticipantMessageData}
import slick.lifted.ProvenShape

trait ParticipantMessageDbTableDefinitions extends BaseDbTableDefinitions {

  import profile.api._

  class ParticipantMessages(tag: Tag) extends RecordTable[ParticipantMessageData](tag, "participant_message") {

    def senderId: Rep[String]             = column[String]("sender_id")
    def receiverId: Rep[String]           = column[String]("receiver_id")
    def message: Rep[Option[String]]      = column[Option[String]]("message")
    def rating: Rep[Option[Int]]          = column[Option[Int]]("rating")
    def invoiceId: Rep[Option[InvoiceId]] = column[Option[InvoiceId]]("invoice_id")

    private[dao] def data = (senderId, receiverId, message, rating, invoiceId).mapTo[ParticipantMessageData]

    override def * : ProvenShape[ParticipantMessage] = record(data)

  }

}
