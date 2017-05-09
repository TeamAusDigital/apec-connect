package org.ausdigital.apecconnect.users.controllers.transport

import play.api.libs.json.{ Format, Json }

/**
  * API response that is sent upon a successful sign in. Contains the information required for the client to continue
  * interacting with the system as the signed in user.
  *
  * @param token the token that should be used by the client for subsequent interactions with the system.
  */
final case class SignInApiResponse(token: String)

/**
  * Companion that contains the JSON formatter.
  */
object SignInApiResponse {
  implicit val format: Format[SignInApiResponse] = Json.format[SignInApiResponse]
}

