package org.ausdigital.apecconnect.businessregister.model

import play.api.libs.json.{Json, OFormat}

/**
  * Response from APEC Connect Business Register for Participants (business) lookup.
  * @param count number of results returned.
  * @param results matched Participants from the lookup query.
  */
final case class LookupParticipantsResponse(
    count: Int,
    results: Seq[ParticipantResult]
)

/**
  * Provides JSON formatter for LookupParticipantsResponse.
  */
object LookupParticipantsResponse {
  implicit val format: OFormat[LookupParticipantsResponse] = Json.format[LookupParticipantsResponse]
}
