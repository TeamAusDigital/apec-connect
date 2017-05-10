package org.ausdigital.apecconnect.participants.services

import javax.inject.Inject

import com.google.inject.Singleton
import org.ausdigital.apecconnect.db.services.SimpleRecordService
import org.ausdigital.apecconnect.participants.dao.ParticipantDao
import org.ausdigital.apecconnect.participants.model.Participant.ParticipantData
import play.api.Configuration
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext

@Singleton
class ParticipantService @Inject() (override val dao: ParticipantDao, ws: WSClient, configuration: Configuration)(implicit val executionContext: ExecutionContext) extends SimpleRecordService[ParticipantData] {

}