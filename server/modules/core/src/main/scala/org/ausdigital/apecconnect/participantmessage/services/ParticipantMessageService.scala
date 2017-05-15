package org.ausdigital.apecconnect.participantmessage.services

import javax.inject.{Inject, Singleton}

import org.ausdigital.apecconnect.db.services.SimpleRecordService
import org.ausdigital.apecconnect.invoice.dao.InvoiceDao
import org.ausdigital.apecconnect.participantmessage.dao.ParticipantMessageDao
import org.ausdigital.apecconnect.participantmessage.model.ParticipantMessage.{ParticipantMessageData, ParticipantMessageDetails}
import org.ausdigital.apecconnect.participants.model.Participant.Participant
import org.ausdigital.apecconnect.invoice.services.InvoiceService

import scala.concurrent.{ExecutionContext, Future}

/**
  * Finds and creates Participant Messages.
  */
@Singleton
class ParticipantMessageService @Inject()(invoiceService: InvoiceService, invoiceDao: InvoiceDao, override val dao: ParticipantMessageDao)(implicit val executionContext: ExecutionContext)
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
}
