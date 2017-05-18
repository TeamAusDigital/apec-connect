package org.ausdigital.apecconnect.participants.services

import javax.inject.Inject

import com.google.inject.Singleton
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import org.ausdigital.apecconnect.businessregister._
import org.ausdigital.apecconnect.db.services.SimpleRecordService
import org.ausdigital.apecconnect.participants.dao.ParticipantDao
import org.ausdigital.apecconnect.participants.model.Participant.{Participant, ParticipantData}
import org.ausdigital.apecconnect.participants.model.{Participant, ParticipantIdentity}
import org.ausdigital.apecconnect.db.model.RecordOps._
import play.api.Configuration
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

/**
  * Finds, creates and deletes Participants.
  */
@Singleton
class ParticipantService @Inject()(override val dao: ParticipantDao, ws: WSClient, configuration: Configuration)(implicit val executionContext: ExecutionContext)
    extends SimpleRecordService[ParticipantData]
    with IdentityService[ParticipantIdentity] {

  override def retrieve(loginInfo: LoginInfo): Future[Option[ParticipantIdentity]] =
    dao
      .run {
        dao.findByIdentifier(loginInfo.providerKey)
      }
      .map { maybeParticipant =>
        maybeParticipant.map { participant =>
          ParticipantIdentity(participant.id, LoginInfo(ApecConnectBusinessRegisterAuthProvider, participant.identifier))
        }
      }

  def findByUsername(username: String): Future[Option[Participant]] = dao.run {
    dao.findByUsername(username)
  }

  def queryByBusinessName(businessName: String): Future[Seq[Participant]] = dao.run {
    dao.queryByBusinessName(businessName).map { participants =>
      participants.map(Participant.publicParticipantView)
    }
  }

  def findByIdentifier(identifier: String): Future[Option[Participant]] = dao.run {
    dao.findByIdentifier(identifier)
  }

  def allParticipants(): Future[Seq[Participant]] = dao.run {
    dao.fetchAll().map { participants =>
      participants.map(Participant.publicParticipantView)
    }
  }
}
