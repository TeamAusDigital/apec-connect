package org.ausdigital

import au.com.agiledigital.rest.controllers.transport.{JsonApiResponse, Message, MessageLevel}
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results.BadRequest

import scalaz.NonEmptyList

package object apecconnect {

  /**
    * Converts a non-empty list of errors into a Result.
    * @return 400 bad request with aggregated errors.
    */
  def nelAsResponse: (NonEmptyList[String]) => Result = errors => {
    val response = errors.tail.foldLeft(JsonApiResponse[String](result = None, message = Message(errors.head, MessageLevel.Alert))) {
      case (r, error) => r.withMessage(Message(error, MessageLevel.Alert))
    }
    BadRequest(Json.toJson(response))
  }

}
