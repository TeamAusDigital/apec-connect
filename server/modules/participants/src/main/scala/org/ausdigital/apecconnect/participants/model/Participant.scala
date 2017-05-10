package org.ausdigital.apecconnect.participants.model

import enumeratum.{ EnumEntry, PlayEnum }
import org.ausdigital.apecconnect.common.model.ApecConnectEnumEntry
import org.ausdigital.apecconnect.db.model.{ Record, RecordId }
import play.api.libs.json.{ Json, OFormat }

import scala.collection.immutable.IndexedSeq
import scalaz.Equal

sealed abstract class VerificationStatus(val value: Int, val name: String) extends EnumEntry with ApecConnectEnumEntry

object VerificationStatus extends PlayEnum[VerificationStatus] {

  case object NotVerified extends VerificationStatus(1, "NOT_VERIFIED")

  case object Verified extends VerificationStatus(2, "VERIFIED")

  override val values: IndexedSeq[VerificationStatus] = findValues

  implicit val equal: Equal[VerificationStatus] = Equal.equalA
}

sealed abstract class AccountStatus(val value: Int, val name: String) extends EnumEntry with ApecConnectEnumEntry

object AccountStatus extends PlayEnum[AccountStatus] {

  case object Enabled extends AccountStatus(1, "ENABLED")

  case object Disabled extends AccountStatus(2, "DISABLED")

  override val values: IndexedSeq[AccountStatus] = findValues

  implicit val equal: Equal[AccountStatus] = Equal.equalA
}

object Participant {

  final case class ParticipantData(
    identifier: String,
    businessName: String,
    email: Option[String],
    phone: Option[String],
    authToken: String,
    verificationStatus: VerificationStatus = VerificationStatus.NotVerified,
    accountStatus: AccountStatus = AccountStatus.Enabled
  )

  type Participant = Record[ParticipantData]

  type ParticipantId = RecordId[Participant]

  implicit val format: OFormat[ParticipantData] = Json.format[ParticipantData]
}
