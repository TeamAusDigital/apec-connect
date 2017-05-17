package org.ausdigital.apecconnect.announcements.controllers

import javax.inject.Inject

import au.com.agiledigital.rest.controllers.transport.JsonApiResponse
import org.ausdigital.apecconnect.announcements.services.AnnouncementService
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext

class AnnouncementsController @Inject() (
  announcementService: AnnouncementService
)(implicit ec: ExecutionContext) {

  def announcements(): Action[AnyContent] = Action.async { request =>
    for {
      announcements <- announcementService.getAnnouncements
    } yield JsonApiResponse.buildResponse(s"Retrieved public announcements.", announcements)
  }
}
