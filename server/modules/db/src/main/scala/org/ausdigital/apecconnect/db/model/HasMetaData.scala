package org.ausdigital.apecconnect.db.model

import java.time.LocalDateTime

import au.com.agiledigital.rest.json.EnumerationFormat
import org.ausdigital.apecconnect.db.model.RecordStatus._
import play.api.libs.json._

trait HasMetaData[M] {
  val metaData: MetaData
  def updateMetaData(metaData: MetaData = metaData): M
}

/**
  * Base metadata for DB entities.
  */
final case class MetaData(
  recordStatus: RecordStatus,
  dateCreated: LocalDateTime,
  lastUpdated: LocalDateTime,
  version: Long
)

object MetaData {
  implicit val recordStatusFormat: Format[RecordStatus] = EnumerationFormat.format(RecordStatus)

  implicit val format: OFormat[MetaData] = Json.format[MetaData]

  def tupled: ((RecordStatus, LocalDateTime, LocalDateTime, Long)) => MetaData = (MetaData.apply _).tupled
}

