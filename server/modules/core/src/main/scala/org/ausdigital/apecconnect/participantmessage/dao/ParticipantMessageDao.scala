package org.ausdigital.apecconnect.participantmessage.dao

import java.time.Clock
import javax.inject.Inject

import org.ausdigital.apecconnect.db.dao.RecordDao
import org.ausdigital.apecconnect.participantmessage.model.ParticipantMessage.{ParticipantMessage, ParticipantMessageData}
import org.ausdigital.apecconnect.participants.model.Participant.Participant
import org.ausdigital.apecconnect.db.model.RecordOps._
import play.api.db.slick.DatabaseConfigProvider

class ParticipantMessageDao @Inject() (override val dbConfigProvider: DatabaseConfigProvider, override val clock: Clock)
  extends RecordDao[ParticipantMessageData] with ParticipantMessageDbTableDefinitions {

  import profile.api._

  override type EntityTable = ParticipantMessages

  override def tableQuery: TableQuery[ParticipantMessages] = TableQuery[ParticipantMessages]

  /**
    * TODO: add filters to reduce results set.
    */
  def queryParticipantMessages(participant: Participant): DBIO[Seq[ParticipantMessage]]= {
    baseQuery.filter { p =>
      p.senderId === participant.identifier || p.receiverId === participant.identifier
    }.result
  }

}
