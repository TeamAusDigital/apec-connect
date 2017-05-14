package org.ausdigital.apecconnect.participantmessage.services

import javax.inject.{Inject, Singleton}

import org.ausdigital.apecconnect.db.services.SimpleRecordService
import org.ausdigital.apecconnect.participantmessage.dao.ParticipantMessageDao
import org.ausdigital.apecconnect.participantmessage.model.ParticipantMessage.{ParticipantMessage, ParticipantMessageData}
import org.ausdigital.apecconnect.participants.model.Participant.Participant

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ParticipantMessageService  @Inject()(override val dao: ParticipantMessageDao)(implicit val executionContext: ExecutionContext)
  extends SimpleRecordService[ParticipantMessageData] {

  def queryParticipantMessages(participant: Participant): Future[Seq[ParticipantMessage]] = dao.run {
    dao.queryParticipantMessages(participant)
  }

}
