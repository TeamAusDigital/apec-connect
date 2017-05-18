package org.ausdigital.apecconnect.web.controllers

import javax.inject.Inject

import org.ausdigital.apecconnect.announcements.model.Announcement.AnnouncementData
import org.ausdigital.apecconnect.announcements.services.AnnouncementService
import org.ausdigital.apecconnect.participantmessage.services.ParticipantMessageService
import org.ausdigital.apecconnect.participants.services.ParticipantService
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.ExecutionContext

/**
  * Provides the administrative portal pages for use by business officials
  */
class WebPortalController @Inject() (
  participantService: ParticipantService,
  announcementService: AnnouncementService,
  participantMessageService: ParticipantMessageService
)(implicit ec: ExecutionContext) extends Controller {

  def index(): Action[AnyContent] = Action { request =>
    Ok(views.html.index())
  }

  def participants(): Action[AnyContent] = Action.async { request =>
    participantService.allParticipants().map { participants =>
      Ok(views.html.businesses(participants))
    }
  }

  def transactions(): Action[AnyContent] = Action.async { request =>
    participantMessageService.fetchAllWithInvoice().map { messages =>
      val transactions = messages flatMap { message =>
        message.invoice.map(i => (i.data, message.sender.data, message.receiver.data))
      }
      Ok(views.html.transactions(transactions))
    }
  }

  def announcements(): Action[AnyContent] = Action.async { request =>
    announcementService.getAnnouncements.map { announcements =>
      Ok(views.html.announcements(announcements.reverse))
    }
  }

  // TODO: authentication here, actual admin users, use the user's id for this
  val adminUser = "admin"

  val announcementForm: Form[AnnouncementData] = Form(
    mapping("message" -> text)
    { message => AnnouncementData(adminUser, message) }
    { data => Some(data.message) }
  )

  def sendAnnouncement(): Action[AnnouncementData] = Action.async(parse.form(announcementForm)) { request =>
    for {
      _ <- announcementService.sendAnnouncement(request.body)
    } yield Redirect(routes.WebPortalController.announcements())
  }
}
