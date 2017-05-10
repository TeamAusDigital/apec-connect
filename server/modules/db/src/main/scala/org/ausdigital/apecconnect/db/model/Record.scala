package org.ausdigital.apecconnect.db.model

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc.PathBindable

import scala.language.implicitConversions
import scalaz.Equal

/**
 * Container for persisted piece of data, its id and metadata.
 * @param id the id of the persisted data.
 * @param data the persisted data.
 * @param metaData the metadata.
 * @tparam M the type of the persisted data.
 */
final case class Record[M](id: RecordId[Record[M]], data: M, metaData: MetaData) extends HasMetaData[Record[M]] with Identifiable[RecordId[Record[M]], Record[M]] {

  override def updateMetaData(metaData: MetaData = metaData): Record[M] = copy(metaData = metaData)

  override def updateId(id: RecordId[Record[M]]): Record[M] = copy(id = id)

  def updateData(f: (M) => M): Record[M] = copy(data = f(data))

}

/**
 * Companion that contains the JSON writer.
 */
object Record {

  /**
   * Writes for Records that flattens the data down to the same level as the id.
   *
   * @param dataWrites the object writer for the data contained in the record.
   * @tparam M the type of the data contained in the record.
   * @return the writes for the Record containing an M.
   */
  implicit def writes[M](implicit dataWrites: OWrites[M]): Writes[Record[M]] = new Writes[Record[M]] {
    override def writes(o: Record[M]): JsValue =
      dataWrites.writes(o.data) ++ Json.obj("id" -> o.id, "metaData" -> o.metaData)
  }

  /**
   * Reads from the JSON format of the Record and convert it to a persisted data model.
   *
   * @param dataReads the object reader for the data.
   * @tparam M the type of the data contained in the record.
   * @return the reads of the Record.
   */
  implicit def reads[M](implicit dataReads: Reads[M]): Reads[Record[M]] =
    (
      (JsPath \ "id").read[RecordId[Record[M]]] and
      JsPath.read[M] and
      (JsPath \ "metaData").read[MetaData]
    )((id, data, metaData) => Record[M](id, data, metaData))

}

/**
 * Identifier for a Record. Means by which a Record becomes Identifiable.
 *
 * @param value the identifier of the record.
 * @tparam M the type identified by this identifier.
 */
final case class RecordId[M](value: Long) extends AnyVal

object RecordId {
  implicit def format[M]: Format[RecordId[M]] = new Format[RecordId[M]] {
    override def writes(o: RecordId[M]): JsValue = JsNumber(o.value)

    override def reads(json: JsValue): JsResult[RecordId[M]] = json.validate[Long].map(RecordId(_))
  }

  /**
   * Allows record ids to be parsed from URL paths (e.g. in route files).
   */
  implicit def pathBinder[M](implicit longBinder: PathBindable[Long]): PathBindable[RecordId[M]] = new PathBindable[RecordId[M]] {

    override def bind(key: String, value: String): Either[String, RecordId[M]] = longBinder.bind(key, value).right.map(RecordId.apply)

    override def unbind(key: String, recordId: RecordId[M]): String = longBinder.unbind(key, recordId.value)

  }

  implicit def ordering[M]: Ordering[RecordId[M]] = Ordering.by { recordId: RecordId[M] =>
    recordId.value
  }

  implicit def equals[M]: Equal[RecordId[M]] = Equal.equalA[RecordId[M]]
}

/**
 * Implicits and type-class implementations for [[Record]].
 */
trait RecordOps {

  import scalaz._

  implicit val recordEqual: Equal[Record[Int]] = Equal.equalA

  implicit def recordToData[A](record: Record[A]): A = record.data
}

object RecordOps extends RecordOps
