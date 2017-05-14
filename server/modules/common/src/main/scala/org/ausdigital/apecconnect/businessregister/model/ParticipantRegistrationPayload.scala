package org.ausdigital.apecconnect.businessregister.model

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Participant registration payload that is required for sign up with APEC Connect Business Register.
  * This is the payload received by APEC Connect application.
  * @param businessName name of the participant's business.
  * @param email of the participant.
  * @param phone of the phone number.
  * @param username of the participant, note that this is a required unique field by APEC Connect Business Register API.
  */
final case class ParticipantRegistrationPayload(businessName: String, email: Option[String], phone: Option[String], username: Option[String])

/**
  * Provides JSON formatter for the ParticipantRegistrationPayload.
  */
object ParticipantRegistrationPayload {

  implicit val registrationWrites: Writes[ParticipantRegistrationPayload] = (
    (JsPath \ "businessName").write[String] and
    (JsPath \ "email").writeNullable[String] and
    (JsPath \ "phone").writeNullable[String] and
    (JsPath \ "username").writeNullable[String]
  )(unlift(ParticipantRegistrationPayload.unapply))

  implicit val registrationReads: Reads[ParticipantRegistrationPayload] = (
    (JsPath \ "businessName").read[String] and
      (JsPath \ "email").readNullable[String] and
      (JsPath \ "phone").readNullable[String] and
      (JsPath \ "username").readNullable[String]
    )(ParticipantRegistrationPayload.apply _)
}
