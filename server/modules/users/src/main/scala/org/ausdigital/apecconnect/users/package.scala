package org.ausdigital.apecconnect

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.actions.SecuredRequest

package object users {

  /**
    * Silhouette type used by the application. Defines the authentication environment.
    */
  type ApecConnectSilhouette = Silhouette[ApecConnectAuthEnv]

  /**
    * Specialised [[SecuredRequest]] that takes a single type parameter (that describes the request). Makes it easier
    * to use a SecuredRequest in ActionTransformers.
    *
    * @tparam A the type of the request body.
    */
  type ApecConnectSecuredRequest[A] = SecuredRequest[ApecConnectAuthEnv, A]

}