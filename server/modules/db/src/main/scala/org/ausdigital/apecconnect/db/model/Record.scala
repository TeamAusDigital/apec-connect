package org.ausdigital.apecconnect.db.model

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

import scala.language.implicitConversions

/**
  * Container for persisted piece of data, its id and metadata.
  * @param id the id of the persisted data.
  * @param data the persisted data.
  * @param metaData the metadata.
  * @tparam M the type of the persisted data.
  */
final case class Record[M](id: Long, data: M, metaData: MetaData)
    extends HasMetaData[Record[M]] with Identifiable[Record[M]] {

  override def updateMetaData(metaData: MetaData = metaData): Record[M] = copy(metaData = metaData)

  override def updateId(id: Long): Record[M] = copy(id = id)
}

/**
  * Companion that contains the JSON writer.
  */
object Record {

  /**
    * Writes for Records that flattens the data down to the same level as the id.
    * @param dataWrites the object writer for the data contained in the record.
    * @tparam M the type of the data contained in the record.
    * @return the writes for the Record containing an M.
    */
  implicit def writes[M](implicit dataWrites: OWrites[M]): Writes[Record[M]] = new Writes[Record[M]] {
    override def writes(o: Record[M]): JsValue = {
      dataWrites.writes(o.data) ++ Json.obj("id" -> o.id, "metaData" -> o.metaData)
    }
  }

  /**
    * Reads from the JSON format of the Record and convert it to a persisted data model.
    * @param dataReads the object reader for the data.
    * @tparam M the type of the data contained in the record.
    * @return the reads of the Record.
    */
  implicit def reads[M](implicit dataReads: Reads[M]): Reads[Record[M]] = (
    (JsPath \ "id").read[Long] and
    JsPath.read[M] and
    (JsPath \ "metaData").read[MetaData]
  )(Record[M](_, _, _))

  implicit def convert[M, N](r: Record[M])(implicit conv: M => N): Record[N] = Record[N](
    id = r.id,
    metaData = r.metaData,
    data = conv(r.data)
  )
}
