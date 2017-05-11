package org.ausdigital.apecconnect.businessregister.model

import play.api.libs.json.{ Json, OFormat }

/**
  * Response that returned from APEC Connect Business Register.
  * Note that we don't need all fields from the response yet.
  * @see https://github.com/TeamAusDigital/apec-connect-cloud-ledger/blob/master/business-register/API.md
  * @param uuid id of the registered participant.
  * @param username of the registered participant.
  * @param access_token for the registered participant. Note that this is not used at the moment, but maybe
  *                     useful in the future for temporarily login purpose.
  */
final case class ParticipantRegistrationResponse(uuid: String, username: String, access_token: String)

/**
  * Provides JSON formatter for the ParticipantRegistrationResponse.
  */
object ParticipantRegistrationResponse {
  implicit val format: OFormat[ParticipantRegistrationResponse] = Json.format[ParticipantRegistrationResponse]
}
