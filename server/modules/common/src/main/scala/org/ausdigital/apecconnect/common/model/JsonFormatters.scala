package org.ausdigital.apecconnect.common.model

import java.util.Date

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import play.api.libs.json.{Format, JsString, JsSuccess, JsValue}

object JsonFormatters {

  def dateFormatter(formatter: DateTimeFormatter): Format[DateTime] = new Format[DateTime] {
    def reads(json: JsValue): JsSuccess[DateTime] = json match {
      case JsString(string) => JsSuccess(new DateTime(formatter.parseDateTime(string)))
      case unknown => throw new IllegalArgumentException("DateFormatter expects dates as JsString. Got [" + unknown + "].")
    }

    def writes(date: DateTime): JsValue = JsString(formatter.print(date))

    override def toString: String = s"F $formatter"
  }

  /**
    * JSON formatter for ISO8601 dates and times.
    */
  implicit object DateFormatter extends Format[Date] {

    val parser = ISODateTimeFormat.dateTime()

    def reads(json: JsValue) = json match {
      case JsString(string) => JsSuccess(parser.parseDateTime(string).toDate)
      case unknown => throw new IllegalArgumentException("DateFormatter expects dates as JsString. Got [" + unknown + "].")
    }

    def writes(date: Date) = JsString(parser.print(new DateTime(date)))
  }

  /**
    * JSON formatter for ISO8601 dates and times as Joda DateTimes
    */
  implicit val jodaDateTimeFormatter: Format[DateTime] = dateFormatter(ISODateTimeFormat.dateTime())

}
