package org.ausdigital.apecconnect.businessregister.model

import play.api.libs.json.{ Json, OFormat }

/**
 * @see https://github.com/TeamAusDigital/apec-connect-cloud-ledger/blob/master/business-register/API.md
 */
final case class ParticipantRegistrationResponse(uuid: String, username: String, access_token: String)

object ParticipantRegistrationResponse {
  implicit val format: OFormat[ParticipantRegistrationResponse] = Json.format[ParticipantRegistrationResponse]
}
