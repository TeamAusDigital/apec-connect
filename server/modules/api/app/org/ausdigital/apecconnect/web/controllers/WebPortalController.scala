package org.ausdigital.apecconnect.web.controllers

import javax.inject.Inject

import play.api.mvc.{Action, AnyContent, Controller}

/**
  * Provides the administrative portal pages for use by business officials
  */
class WebPortalController @Inject() () extends Controller {

  def index(): Action[AnyContent] = Action { request =>
    Ok(views.html.index())
  }

  def participants(): Action[AnyContent] = Action { request =>
    Ok(views.html.participants())
  }

  def announcements(): Action[AnyContent] = Action { request =>
    Ok(views.html.announcements())
  }
}
