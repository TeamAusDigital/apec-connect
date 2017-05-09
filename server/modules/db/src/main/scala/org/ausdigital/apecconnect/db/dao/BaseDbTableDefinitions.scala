package org.ausdigital.apecconnect.db.dao

import java.sql.Timestamp
import java.time.{ LocalDate, LocalDateTime }

import org.ausdigital.apecconnect.db.model.RecordStatus._
import org.ausdigital.apecconnect.db.model.{ RecordStatus, _ }
import play.api.libs.json.{ JsValue, Json }
import slick.driver.JdbcProfile
import slick.jdbc.JdbcType
import slick.lifted.{ MappedProjection, ProvenShape }

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

/**
  * Table definitions for tables with standard metadata and id columns. Also includes mappers for common types.
  */
trait BaseDbTableDefinitions {

  protected val driver: JdbcProfile

  import driver.api._

  /**
    * Maps a [[RecordStatus]] to its int representation for storage in the RDBMS.
    */
  implicit lazy val recordStatusMapper: JdbcType[RecordStatus] = EnumerationMapper.mapper(RecordStatus)

  /**
    * Maps a [[LocalDateTime]] to a [[Timestamp]].
    */
  implicit lazy val localDateTimeMapper: JdbcType[LocalDateTime] = MappedColumnType.base[LocalDateTime, Timestamp](
    localDateTime => Timestamp.valueOf(localDateTime),
    timeStamp => timeStamp.toLocalDateTime
  )

  /**
    * Maps a [[LocalDate]] to a [[Timestamp]].
    */
  implicit lazy val localDateMapper: JdbcType[LocalDate] = MappedColumnType.base[LocalDate, Timestamp](
    localDate => Timestamp.valueOf(localDate.atStartOfDay),
    timeStamp => timeStamp.toLocalDateTime.toLocalDate
  )

  /**
    * Maps a [[JsValue]] to a [[String]].
    */
  implicit lazy val jsonMapper: JdbcType[JsValue] = MappedColumnType.base[JsValue, String](
    json => Json.stringify(json),
    s => Json.parse(s)
  )

  /**
    * Base table definition that includes row metadata.
    */
  trait TableWithMetaData[A <: HasMetaData[A]] extends Table[A] {

    def recordStatus: Rep[RecordStatus] = column[RecordStatus]("record_status")

    def dateCreated: Rep[LocalDateTime] = column[LocalDateTime]("date_created")

    def lastUpdated: Rep[LocalDateTime] = column[LocalDateTime]("last_updated")

    def version: Rep[Long] = column[Long]("version")

    /**
      * Projection between [[MetaData]] and the default metadata columns. Can be used as a convenience
      * to map the embedded metadata.
      */
    def meta: MappedProjection[MetaData, (RecordStatus, LocalDateTime, LocalDateTime, Long)] =
      (recordStatus, dateCreated, lastUpdated, version) <> (MetaData.tupled, MetaData.unapply)
  }

  trait TableWithId[A <: Identifiable[A]] extends Table[A] {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  }

  abstract class RecordTable[A](tag: Tag, name: String) extends Table[Record[A]](tag, name) with TableWithMetaData[Record[A]] with TableWithId[Record[A]] {

    type DataType

    def data: MappedProjection[A, DataType]

    override def * : ProvenShape[Record[A]] =
      (id, data, meta).<>[Record[A], (Long, A, MetaData)](
        {
          case (id, d, meta) => Record[A](id, d, meta)
        }, {
          case Record(id, d, meta) => Some((id, d, meta))
        }
      )
  }

  /**
    * Checks whether the record is active.
    * @param tableWithMetadata the table that contains the metadata.
    * @tparam A the type of record contained in the table.
    * @return a lifted Boolean that will be true if the record is active, otherwise false.
    */
  protected def isActive[A <: HasMetaData[A]](tableWithMetadata: TableWithMetaData[A]): Rep[Boolean] =
    tableWithMetadata.recordStatus === RecordStatus.Active
}

/**
  * Creates slick mappers for enumerations.
  */
object EnumerationMapper {
  /**
    * Creates a slick mapper / type for the provided enumeration.
    * @param e the enumeration to be mapped.
    * @param driver the driver that will support the mapping.
    * @tparam A the type of the enumeration to be mapped.
    * @return the mapper / type.
    */
  def mapper[A <: Enumeration](e: A)(implicit driver: JdbcProfile): JdbcType[A#Value] = {
    import driver.api._
    MappedColumnType.base[A#Value, Int](
      a => a.id,
      id => e.apply(id)
    )
  }
}

