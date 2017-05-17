package org.ausdigital.apecconnect.businessregister.model

import play.api.libs.json._

/**
  * Participant registration payload that is required for sign up with APEC Connect Business Register.
  * This is the payload received by APEC Connect application.
  * @param businessName name of the participant's business.
  * @param economy represents economy locale of the business, a country code.
  * @param email of the participant.
  * @param phone of the phone number.
  * @param username of the participant, note that this is a required unique field by APEC Connect Business Register API.
  */
final case class ParticipantRegistrationPayload(businessName: String, economy: String, email: Option[String], phone: Option[String], username: Option[String])

/**
  * Provides JSON formatter for the ParticipantRegistrationPayload.
  */
object ParticipantRegistrationPayload {
  implicit val format: OFormat[ParticipantRegistrationPayload] = Json.format[ParticipantRegistrationPayload]
}
