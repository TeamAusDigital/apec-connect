package org.ausdigital.apecconnect.participantmessage.services

import javax.inject.{Inject, Singleton}

import org.ausdigital.apecconnect.db.services.SimpleRecordService
import org.ausdigital.apecconnect.invoice.dao.InvoiceDao
import org.ausdigital.apecconnect.participantmessage.dao.ParticipantMessageDao
import org.ausdigital.apecconnect.participantmessage.model.ParticipantMessage.{ParticipantMessageData, ParticipantMessageDetails}
import org.ausdigital.apecconnect.participants.model.Participant.Participant
import org.ausdigital.apecconnect.invoice.services.InvoiceService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ParticipantMessageService @Inject()(invoiceService: InvoiceService, invoiceDao: InvoiceDao, override val dao: ParticipantMessageDao)(implicit val executionContext: ExecutionContext)
    extends SimpleRecordService[ParticipantMessageData] {

  def queryParticipantMessages(participant: Participant): Future[Seq[ParticipantMessageDetails]] = dao.run {
    for {
      messagesSent     <- dao.queryMessageFromParticipant(sender = participant)
      messagesReceived <- dao.queryMessageToParticipant(receiver = participant)
    } yield messagesSent ++ messagesReceived
  }
}
