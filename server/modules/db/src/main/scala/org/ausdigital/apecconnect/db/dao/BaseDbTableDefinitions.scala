package org.ausdigital.apecconnect.db.dao

import java.sql.Timestamp
import java.time.{LocalDate, LocalDateTime}

import org.ausdigital.apecconnect.db.model.{RecordStatus, _}
import org.joda.money.{CurrencyUnit, Money}
import org.joda.time.DateTime
import play.api.libs.json.{JsValue, Json}
import slick.jdbc.{JdbcProfile, JdbcType}
import slick.lifted.{MappedProjection, ProvenShape}

/**
  * Table definitions for tables with standard metadata and id columns. Also includes mappers for common types.
  */
trait BaseDbTableDefinitions {

  protected val profile: JdbcProfile

  import profile.api._

  val like: (Rep[_], Rep[_]) => Rep[Boolean] = SimpleBinaryOperator[Boolean]("like")

  /**
    * Maps a [[RecordStatus]] to its int representation for storage in the RDBMS.
    */
  implicit lazy val recordStatusMapper: JdbcType[RecordStatus] = MappedColumnType.base[RecordStatus, Int](
    a => a.value,
    id => RecordStatus.withValue(id)
  )

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
    * Maps a [[DateTime]] to a [[Timestamp]].
    */
  implicit lazy val jodaDateTimeMapper: JdbcType[DateTime] = MappedColumnType.base[DateTime, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    timeStamp => new DateTime(timeStamp)
  )

  /**
    * Maps a [[JsValue]] to a [[String]].
    */
  implicit lazy val jsonMapper: JdbcType[JsValue] = MappedColumnType.base[JsValue, String](
    json => Json.stringify(json),
    s => Json.parse(s)
  )

  // TODO: format different currency type. Defaults to AUD when get the money for now.
  implicit val moneyType: JdbcType[Money] = MappedColumnType.base[Money, BigDecimal](
    money => money.getAmount,
    amount => Money.of(CurrencyUnit.AUD, amount.bigDecimal)
  )

  implicit def recordIdMapper[A]: JdbcType[RecordId[A]] = MappedColumnType.base[RecordId[A], Long](
    recordId => recordId.value,
    id => new RecordId[A](id)
  )

  /**
    * Base table definition that includes row metadata.
    */
  trait TableWithMetaData[A <: HasMetaData[A]] extends Table[A] {

    def recordStatus: Rep[RecordStatus] = column[RecordStatus]("record_status")

    def dateCreated: Rep[DateTime] = column[DateTime]("created_at")

    def lastUpdated: Rep[DateTime] = column[DateTime]("last_updated")

    def version: Rep[Long] = column[Long]("version")

    /**
      * Projection between [[MetaData]] and the default metadata columns. Can be used as a convenience
      * to map the embedded metadata.
      */
    def meta: MappedProjection[MetaData, (RecordStatus, DateTime, DateTime, Long)] =
      (recordStatus, dateCreated, lastUpdated, version) <> (MetaData.tupled, MetaData.unapply)
  }

  /**
    * Base table definition that includes the autoinc, primary key id column.
    */
  trait TableWithId[Id, A <: Identifiable[Id, A]] extends Table[A] {

    protected[this] implicit def idMapper: JdbcType[Id]

    def id: Rep[Id] = column[Id]("id", O.PrimaryKey, O.AutoInc)
  }

  /**
    * Definition of a table that contains a [[Record]], including the id and metadata.
    */
  abstract class RecordTable[A](tag: Tag, name: String) extends Table[Record[A]](tag, name) with TableWithMetaData[Record[A]] with TableWithId[RecordId[Record[A]], Record[A]] {

    override protected[this] implicit def idMapper: JdbcType[RecordId[Record[A]]] = MappedColumnType.base[RecordId[Record[A]], Long](
      recordId => recordId.value,
      id => new RecordId[Record[A]](id)
    )

    def record[B](data: MappedProjection[A, B]): ProvenShape[Record[A]] = (id, data, meta).<>(
      ((Record.apply[A] _).tupled),
      Record.unapply[A]
    )

  }

  /**
    * Checks whether the record is active.
    *
    * @param tableWithMetadata the table that contains the metadata.
    * @tparam A the type of record contained in the table.
    * @return a lifted Boolean that will be true if the record is active, otherwise false.
    */
  protected def isActive[A <: HasMetaData[A]](tableWithMetadata: TableWithMetaData[A]): Rep[Boolean] =
    tableWithMetadata.recordStatus === (RecordStatus.Active: RecordStatus)
}
