package org.ausdigital.apecconnect.announcements.dao

import java.time.Clock
import javax.inject.Inject

import com.google.inject.Singleton
import org.ausdigital.apecconnect.announcements.model.Announcement.AnnouncementData
import org.ausdigital.apecconnect.db.dao.RecordDao
import org.ausdigital.apecconnect.participants.dao.ParticipantDao
import play.api.db.slick.DatabaseConfigProvider


/**
  * Responsible for persistence of [[org.ausdigital.apecconnect.announcements.model.Announcement]]
  */
@Singleton
class AnnouncementDao @Inject()(participantDao: ParticipantDao, override val dbConfigProvider: DatabaseConfigProvider, override val clock: Clock)
    extends RecordDao[AnnouncementData]
    with AnnouncementDbTableDefinitions {

  import profile.api._

  override type EntityTable = ParticipantMessages

  override def tableQuery: TableQuery[ParticipantMessages] = TableQuery[ParticipantMessages]

}