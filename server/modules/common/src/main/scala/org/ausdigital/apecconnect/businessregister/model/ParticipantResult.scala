package org.ausdigital.apecconnect.businessregister.model

import play.api.libs.json.{Json, OFormat}


final case class ParticipantResult(uuid: String, business_name: String)

object ParticipantResult {
  implicit val format: OFormat[ParticipantResult] = Json.format[ParticipantResult]
}