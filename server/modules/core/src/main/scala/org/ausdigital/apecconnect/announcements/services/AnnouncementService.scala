package org.ausdigital.apecconnect.announcements.services

import javax.inject.{Inject, Singleton}

import org.ausdigital.apecconnect.announcements.dao.AnnouncementDao
import org.ausdigital.apecconnect.announcements.model.Announcement.{Announcement, AnnouncementData}
import org.ausdigital.apecconnect.db.services.SimpleRecordService

import scala.concurrent.{ExecutionContext, Future}

/**
  * Finds and creates Participant Messages.
  */
@Singleton
class AnnouncementService @Inject()(override val dao: AnnouncementDao)(implicit val executionContext: ExecutionContext)
    extends SimpleRecordService[AnnouncementData] {

  /**
    * Finds all broadcast messages
    * @return announcement messages
    */
  def getAnnouncements: Future[Seq[Announcement]] = dao.run {
    dao.fetchAll()
  }

  def sendAnnouncement(data: AnnouncementData): Future[Announcement] = dao.run {
    dao.create(data)
  }
}
