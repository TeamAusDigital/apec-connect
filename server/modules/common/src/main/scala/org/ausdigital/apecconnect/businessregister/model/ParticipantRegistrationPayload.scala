package org.ausdigital.apecconnect.businessregister.model

import play.api.libs.json.{ Json, OFormat }

final case class ParticipantRegistrationPayload(businessName: String, email: Option[String], phone: Option[String], username: Option[String])

object ParticipantRegistrationPayload {
  implicit val format: OFormat[ParticipantRegistrationPayload] = Json.format[ParticipantRegistrationPayload]
}
