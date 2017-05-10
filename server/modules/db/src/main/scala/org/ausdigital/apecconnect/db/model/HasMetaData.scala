package org.ausdigital.apecconnect.db.model

import org.joda.time.DateTime
import play.api.libs.json._

/**
 * Base metadata for DB entities.
 */
final case class MetaData(recordStatus: RecordStatus, dateCreated: DateTime, lastUpdated: DateTime, version: Long)

/**
 * Companion with JSON formatter.
 */
object MetaData {

  implicit val format: OFormat[MetaData] = Json.format[MetaData]

  def tupled: ((RecordStatus, DateTime, DateTime, Long)) => MetaData = (MetaData.apply _).tupled

  val empty: MetaData = MetaData(RecordStatus.Active, new DateTime(0), new DateTime(0), 0)
}

/**
 * Marker trait that identifies types that have metadata and provides a method to update the metadata.
 * @tparam M the type that has metadata.
 */
trait HasMetaData[M] {
  /**
   * Metadata of the type.
   */
  val metaData: MetaData

  /**
   * Updates the metadata and returns the updated containing type.
   * @param metaData the updated metadata.
   * @return the containing type with updated metadata.
   */
  def updateMetaData(metaData: MetaData = metaData): M
}

