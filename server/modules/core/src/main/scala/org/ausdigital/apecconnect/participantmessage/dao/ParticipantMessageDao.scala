package org.ausdigital.apecconnect.participantmessage.dao

import java.time.Clock
import javax.inject.Inject

import com.google.inject.Singleton
import org.ausdigital.apecconnect.db.dao.RecordDao
import org.ausdigital.apecconnect.participantmessage.model.ParticipantMessage.{ParticipantMessageData, ParticipantMessageDetails}
import org.ausdigital.apecconnect.participants.model.Participant.Participant
import org.ausdigital.apecconnect.db.model.RecordOps._
import org.ausdigital.apecconnect.invoice.dao.InvoiceDao
import org.ausdigital.apecconnect.participants.dao.ParticipantDao
import org.ausdigital.apecconnect.participants.model.Participant
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.ExecutionContext

/**
  * Responsible for persistence of [[org.ausdigital.apecconnect.participantmessage.model.ParticipantMessage]]
  */
@Singleton
class ParticipantMessageDao @Inject()(invoiceDao: InvoiceDao, participantDao: ParticipantDao, override val dbConfigProvider: DatabaseConfigProvider, override val clock: Clock)
    extends RecordDao[ParticipantMessageData]
    with ParticipantMessageDbTableDefinitions {

  import profile.api._

  override type EntityTable = ParticipantMessages

  override def tableQuery: TableQuery[ParticipantMessages] = TableQuery[ParticipantMessages]

  def queryMessageToParticipant(receiver: Participant)(implicit ec: ExecutionContext): DBIO[Seq[ParticipantMessageDetails]] =
    baseQuery
      .filter { message =>
        message.receiverId === receiver.identifier
      }
      .join(participantDao.baseQuery)
      .on {
        case (message, sender) => message.senderId === sender.identifier
      }
      .joinLeft(invoiceDao.baseQuery)
      .on {
        case ((message, _), invoice) => message.invoiceId === invoice.id
      }
      .result
      .map {
        _.map {
          case ((message, sender), maybeInvoice) =>
            ParticipantMessageDetails(
              sender = Participant.publicParticipantView(sender),
              receiver = Participant.publicParticipantView(receiver),
              message = message,
              invoice = maybeInvoice
            )
        }
      }

  def queryMessageFromParticipant(sender: Participant)(implicit ec: ExecutionContext): DBIO[Seq[ParticipantMessageDetails]] =
    baseQuery
      .filter { message =>
        message.senderId === sender.identifier
      }
      .join(participantDao.baseQuery)
      .on {
        case (message, receiver) => message.receiverId === receiver.identifier
      }
      .joinLeft(invoiceDao.baseQuery)
      .on {
        case ((message, _), invoice) => message.invoiceId === invoice.id
      }
      .result
      .map {
        _.map {
          case ((message, receiver), maybeInvoice) =>
            ParticipantMessageDetails(
              sender = Participant.publicParticipantView(sender),
              receiver = Participant.publicParticipantView(receiver),
              message = message,
              invoice = maybeInvoice
            )
        }
      }

  def fetchAllWithInvoice()(implicit ec: ExecutionContext): DBIO[Seq[ParticipantMessageDetails]] =
    baseQuery
      .filter(_.invoiceId.nonEmpty)
      .join(participantDao.baseQuery)
      .on {
        case (message, receiver) => message.receiverId === receiver.identifier
      }
      .join(participantDao.baseQuery)
      .on {
        case ((message, _), sender) => message.senderId === sender.identifier
      }
      .joinLeft(invoiceDao.baseQuery)
      .on {
        case (((message, _), _), invoice) => message.invoiceId === invoice.id
      }
      .result
      .map {
        _.map {
          case (((message, receiver), sender), maybeInvoice) =>
            ParticipantMessageDetails(
              sender = Participant.publicParticipantView(sender),
              receiver = Participant.publicParticipantView(receiver),
              message = message,
              invoice = maybeInvoice
            )
        }
      }
}
