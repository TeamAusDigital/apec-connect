package org.ausdigital.apecconnect.businessregister.model

import play.api.libs.json.{Json, OFormat}

final case class LookupParticipantsResponse(
    count: Int,
    results: Seq[ParticipantResult]
)

object LookupParticipantsResponse {
  implicit val format: OFormat[LookupParticipantsResponse] = Json.format[LookupParticipantsResponse]
}
