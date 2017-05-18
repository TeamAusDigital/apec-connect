package org.ausdigital.apecconnect.participants.model

import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }
import enumeratum.{ EnumEntry, PlayEnum }
import org.ausdigital.apecconnect.common.model.ApecConnectEnumEntry
import org.ausdigital.apecconnect.db.model.{ Record, RecordId }
import org.ausdigital.apecconnect.participants.model.Participant.ParticipantId
import play.api.libs.json.{ Json, OFormat }

import scala.collection.immutable.IndexedSeq
import scalaz.Equal

sealed abstract class AccountStatus(val value: Int, val name: String) extends EnumEntry with ApecConnectEnumEntry

object AccountStatus extends PlayEnum[AccountStatus] {

  case object Enabled extends AccountStatus(1, "ENABLED")

  case object Disabled extends AccountStatus(2, "DISABLED")

  override val values: IndexedSeq[AccountStatus] = findValues

  implicit val equal: Equal[AccountStatus] = Equal.equalA
}

/**
  * An Silhouette identify of a Participant.
  * @param id of the Participant.
  * @param loginInfo Silhouette login information.
  */
final case class ParticipantIdentity(id: ParticipantId, loginInfo: LoginInfo) extends Identity

/**
  * Provides JSON formatter for ParticipantIdentity.
  */
object ParticipantIdentity {
  implicit val format: OFormat[ParticipantIdentity] = Json.format[ParticipantIdentity]
}

/**
  * A Participant of APEC Connect application.
  * This entity is integrated with the identity over APEC Connect Business Register.
  */
object Participant {

  final case class ParticipantData(
    identifier: String,
    businessName: String,
    email: Option[String],
    phone: Option[String],
    economy: String,
    username: String,
    authToken: String,
    isVerified: Boolean = false,
    rating: Int = 1,
    accountStatus: AccountStatus = AccountStatus.Enabled
  )

  def publicParticipantView(participant: Participant): Participant = {
    participant.updateData(_.copy(authToken = ""))
  }

  type Participant = Record[ParticipantData]

  type ParticipantId = RecordId[Participant]

  implicit val format: OFormat[ParticipantData] = Json.format[ParticipantData]
}
