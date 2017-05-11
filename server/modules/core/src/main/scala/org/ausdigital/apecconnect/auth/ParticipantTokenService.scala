package org.ausdigital.apecconnect.auth

import javax.inject.Inject

import au.com.agiledigital.rest.controllers.transport.EmptyRequestHeader
import com.mohiva.play.silhouette.api.LoginInfo
import org.ausdigital.apecconnect.businessregister._

import scala.concurrent.{ ExecutionContext, Future }
import scalaz.OptionT.optionT
import scalaz.Scalaz._

class ParticipantTokenService @Inject() (jwtService: JWTService) {

  private implicit val request = new EmptyRequestHeader

  def generateParticipantToken(participantIdentifier: String)(implicit ec: ExecutionContext): Future[Option[String]] = {
    (for {
      participantToken <- optionT(jwtService.create(LoginInfo(ApecConnectBusinessRegisterAuthProvider, participantIdentifier)).map(jwtService.serialize).map(Option.apply))
    } yield participantToken).run
  }

}
