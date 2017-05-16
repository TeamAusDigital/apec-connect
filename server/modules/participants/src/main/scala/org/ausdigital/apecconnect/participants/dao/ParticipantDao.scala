package org.ausdigital.apecconnect.participants.dao

import java.time.Clock
import javax.inject.Inject

import com.google.inject.Singleton
import org.ausdigital.apecconnect.db.dao.RecordDao
import org.ausdigital.apecconnect.participants.model.Participant.{ Participant, ParticipantData }
import play.api.db.slick.DatabaseConfigProvider

/**
  * Responsible for persistence of [[Participant]].
  */
@Singleton
class ParticipantDao @Inject() (override val dbConfigProvider: DatabaseConfigProvider, override val clock: Clock)
    extends RecordDao[ParticipantData] with ParticipantDbTableDefinitions {

  import profile.api._

  override type EntityTable = Participants

  override def tableQuery: TableQuery[Participants] = TableQuery[Participants]

  private implicit def recordIdMapperHighPriority[A] = recordIdMapper[A]

  def findByIdentifier(identifier: String): DBIO[Option[Participant]] =
    baseQuery.filter(_.identifier === identifier).result.headOption

  def findByUsername(username: String): DBIO[Option[Participant]] =
    baseQuery.filter(_.username === username).result.headOption

  def queryByBusinessName(businessName: String): DBIO[Seq[Participant]] =
    baseQuery.filter(_.businessName.toLowerCase like s"%${businessName.toLowerCase}%").result
}
