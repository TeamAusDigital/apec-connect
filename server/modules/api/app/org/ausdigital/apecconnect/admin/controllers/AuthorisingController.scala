package org.ausdigital.apecconnect.admin.controllers

import org.ausdigital.apecconnect.auth.ApecConnectSilhouette

import scala.concurrent.ExecutionContext

trait AuthorisingController {

  def apecConnectSilhouette: ApecConnectSilhouette

  implicit def executionContext: ExecutionContext

  def participantAction = apecConnectSilhouette.SecuredAction

}
