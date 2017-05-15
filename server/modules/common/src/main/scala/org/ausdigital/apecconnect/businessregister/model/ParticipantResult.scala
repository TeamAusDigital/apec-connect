package org.ausdigital.apecconnect.businessregister.model

import play.api.libs.json._

import play.api.libs.functional.syntax._

/**
  * Participant result returned from APEC Connect Business Register.
  * @param participantIdentifier the UUID of the Participant from IBR.
  * @param businessName name of the business that is registered on IBR.
  */
final case class ParticipantResult(participantIdentifier: String, businessName: String)

/**
  * Provides JSON formatter for the ParticipantResult.
  */
object ParticipantResult {
  implicit val registrationWrites: Writes[ParticipantResult] = (
    (JsPath \ "participantIdentifier").write[String] and
      (JsPath \ "businessName").write[String]
    )(unlift(ParticipantResult.unapply))

  implicit val registrationReads: Reads[ParticipantResult] = (
    (JsPath \ "uuid").read[String] and
      (JsPath \ "business_name").read[String]
    )(ParticipantResult.apply _)
}