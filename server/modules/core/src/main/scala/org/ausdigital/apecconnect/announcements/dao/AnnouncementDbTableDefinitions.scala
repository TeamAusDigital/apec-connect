package org.ausdigital.apecconnect.announcements.dao

import org.ausdigital.apecconnect.announcements.model.Announcement.{Announcement, AnnouncementData}
import org.ausdigital.apecconnect.db.dao.BaseDbTableDefinitions
import slick.lifted.ProvenShape

/**
  * Table definitions and associated slick mappings for an Announcement.
  */
trait AnnouncementDbTableDefinitions extends BaseDbTableDefinitions {

  import profile.api._

  class ParticipantMessages(tag: Tag) extends RecordTable[AnnouncementData](tag, "announcement") {

    def senderId: Rep[String]  = column[String]("sender_id")
    def message: Rep[String]   = column[String]("message")

    private[dao] def data = (senderId, message).mapTo[AnnouncementData]

    override def * : ProvenShape[Announcement] = record(data)
  }

}
