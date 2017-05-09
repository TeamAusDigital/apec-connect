package org.ausdigital.apecconnect.users.controllers

import com.mohiva.play.silhouette.api.actions.{ SecuredActionBuilder, UnsecuredActionBuilder }
import org.ausdigital.apecconnect.users.{ ApecConnectAuthEnv, ApecConnectSilhouette }
/**
  * Controller that supports authorisation of learning console users via JWT.
  */
trait AuthorisingController {

  def environment: ApecConnectSilhouette

  /**
    * An action that may only be performed by a learning operations console user.
    */
  def secureUserAction: SecuredActionBuilder[ApecConnectAuthEnv] = environment.SecuredAction

  /**
    * An action that may be performed by any user.
    */
  def unsecuredAction: UnsecuredActionBuilder[ApecConnectAuthEnv] = environment.UnsecuredAction

}