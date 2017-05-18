package org.ausdigital.apecconnect.participantmessage.services

import javax.inject.{Inject, Singleton}

import org.ausdigital.apecconnect.db.services.SimpleRecordService
import org.ausdigital.apecconnect.invoice.dao.InvoiceDao
import org.ausdigital.apecconnect.participantmessage.dao.ParticipantMessageDao
import org.ausdigital.apecconnect.participantmessage.model.ParticipantMessage.{ParticipantMessage, ParticipantMessageData, ParticipantMessageDetails}
import org.ausdigital.apecconnect.participants.model.Participant.Participant
import org.ausdigital.apecconnect.invoice.services.InvoiceService
import org.ausdigital.apecconnect.db.model.RecordOps._
import org.ausdigital.apecconnect.participants.services.ParticipantService

import scala.concurrent.{ExecutionContext, Future}

/**
  * Finds and creates Participant Messages.
  */
@Singleton
class ParticipantMessageService @Inject()(participantService: ParticipantService, invoiceService: InvoiceService, invoiceDao: InvoiceDao, override val dao: ParticipantMessageDao)(implicit val executionContext: ExecutionContext)
    extends SimpleRecordService[ParticipantMessageData] {

  /**
    * Finds all messages that are related to the given Participant, either as a sender or receiver.
    * @param participant that can be either a sender or receiver for the messages to be queried.
    * @return messages details for the given Participant.
    */
  def queryParticipantMessages(participant: Participant): Future[Seq[ParticipantMessageDetails]] = dao.run {
    for {
      messagesSent     <- dao.queryMessageFromParticipant(sender = participant)
      messagesReceived <- dao.queryMessageToParticipant(receiver = participant)
    } yield messagesSent ++ messagesReceived
  }

  def queryMessagesToParticipant(participant: Participant): Future[Seq[ParticipantMessageDetails]] = dao.run {
    dao.queryMessageToParticipant(receiver = participant)
  }

  def fetchAllWithInvoice(): Future[Seq[ParticipantMessageDetails]] = dao.run {
    dao.fetchAllWithInvoice()
  }


  def extractRatingFromMessages(participant: Participant): Future[Int] = dao.run {
    dao.queryMessageToParticipant(receiver = participant).map { messageDetails =>
      val totalMessagesWithRating = messageDetails.count(_.message.rating.isDefined)
      val totalRating = messageDetails.filter(_.message.rating.isDefined).map(_.message.rating.getOrElse(1)).sum
      if (totalMessagesWithRating > 0) {
        totalRating / totalMessagesWithRating
      }
      else {
        participant.rating
      }
    }
  }
}
