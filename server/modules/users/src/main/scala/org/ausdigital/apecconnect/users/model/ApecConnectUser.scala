package org.ausdigital.apecconnect.users.model

import org.ausdigital.apecconnect.users.model.AccountStatus.AccountStatus
import org.ausdigital.apecconnect.users.model.VerificationStatus.VerificationStatus
import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }

object VerificationStatus extends Enumeration {
  type VerificationStatus = Value
  val NotVerified = Value(0, "NOT_VERIFIED")
  val Verified = Value(1, "VERIFIED")
}

object AccountStatus extends Enumeration {
  type AccountStatus = Value
  val Enabled = Value(0, "ENABLED")
  val Disabled = Value(1, "DISABLED")
}

final case class ApecConnectUser(
  id: Long,
  firstName: Option[String],
  lastName: Option[String],
  fullName: Option[String],
  verificationStatus: VerificationStatus,
  accountStatus: AccountStatus,
  loginInfo: LoginInfo
) extends Identity