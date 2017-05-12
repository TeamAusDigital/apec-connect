package org.ausdigital.apecconnect.auth

import javax.inject.Inject

import au.com.agiledigital.rest.controllers.transport.EmptyRequestHeader
import com.mohiva.play.silhouette.api.LoginInfo
import org.ausdigital.apecconnect.businessregister._

import scala.concurrent.{ ExecutionContext, Future }
import scalaz.OptionT.optionT
import scalaz.Scalaz._

/**
  * Provides authentication token related functions for Participants.
  */
class ParticipantTokenService @Inject() (jwtService: JWTService) {

  private implicit val request = new EmptyRequestHeader

  /**
    * Generates JWT token for Participant.
    * @param participantIdentifier uuid of the participant from APEC Connect Business Regsiter.
    * @return generated JWT token, or None if failed.
    */
  def generateParticipantToken(participantIdentifier: String)(implicit ec: ExecutionContext): Future[Option[String]] = {
    (for {
      participantToken <- optionT(jwtService.create(LoginInfo(ApecConnectBusinessRegisterAuthProvider, participantIdentifier)).map(jwtService.serialize).map(Option.apply))
    } yield participantToken).run
  }

}
