package au.com.agiledigital.seed.time.model

import play.api.libs.json.{ Format, Json }

/**
  * Combines several representations of a system's version of now.
  *
  * @param date the date.
  * @param time the date time.
  * @param millis the millis since epoch.
  */
final case class Time(date: String, time: String, millis: Long)

/**
  * Companion that contains the JSON formatter.
  */
object Time {
  implicit val format: Format[Time] = Json.format[Time]
}

