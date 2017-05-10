package org.ausdigital.apecconnect.db.services

import slick.dbio.DBIO

import org.ausdigital.apecconnect.db.DbioOps._
import org.ausdigital.apecconnect.db.dao.BaseDao
import org.ausdigital.apecconnect.db.model.{ HasMetaData, Identifiable, Record, RecordId }

import scala.concurrent.{ ExecutionContext, Future }

import scalaz._
import Scalaz._
import scala.language.implicitConversions

/**
 * Supports CRUDing items where the type of the persisted record and detailed record are the same.
 * @tparam Data the type of the data in the record.
 */
trait SimpleRecordService[Data] extends RecordService[Data, Record[Data]] with SimpleCrudService[RecordId[Record[Data]], Data, Record[Data]]

/**
 * Supports CRUDing items where the type of the persisted items and detailed items are the same.
 *
 * @tparam Pending   the type of the request that will be used to create the item.
 * @tparam Persisted the type of the persisted item.
 */
trait SimpleCrudService[Ident, Pending, Persisted <: HasMetaData[Persisted] with Identifiable[Ident, Persisted]] extends CrudService[Ident, Pending, Persisted, Persisted] {

  override def findDetailsById(id: Ident)(implicit ec: ExecutionContext): Future[Option[Persisted]] = findById(id)

}

/**
 * Supports CRUDing records.
 *
 * @tparam Data    the type of the data in the record.
 * @tparam Details the type of the detailed item.
 */
trait RecordService[Data, Details] extends CrudService[RecordId[Record[Data]], Data, Record[Data], Details] with DataUpdateService[Data, Data]

/**
 * Supports updating the data in a record.
 *
 * @tparam Data the type of the data in the record.
 */
trait DataUpdateService[Pending, Data] {

  def dao: BaseDao[RecordId[Record[Data]], Record[Data], Pending]

  /**
   * Updates the data of the record with the specified id using the supplied function.
   *
   * @param id the id of the record to be updated.
   * @param u  the function to apply to the data in the record.
   * @return a validation failure if the record does not exist, otherwise the updated record.
   */
  def updateData(id: RecordId[Record[Data]])(u: Data => Data)(implicit ec: ExecutionContext): Future[ValidationNel[String, Record[Data]]] = dao.run {
    OptionT[DBIO, Record[Data]](dao.findOptionById(id))
      .toRight(s"[$id] does not exist.")
      .flatMap(existing => EitherT.right[DBIO, String, Record[Data]](dao.update(existing.updateData(u))))
      .run
      .map(_.validationNel)
  }
}

/**
 * Supports CRUDing items.
 *
 * @tparam Pending   the type of the request that will be used to create the item.
 * @tparam Persisted the type of the persisted item.
 * @tparam Details   the type of the detailed item.
 */
trait CrudService[Ident, Pending, Persisted <: HasMetaData[Persisted] with Identifiable[Ident, Persisted], Details]
    extends CreationService[Pending, Persisted]
    with FinderService[Ident, Persisted, Details]
    with UpdateService[Ident, Persisted]
    with DeletionService[Ident] {

  def dao: BaseDao[Ident, Persisted, Pending]

  implicit def runDbio[A](dbio: DBIO[A]): Future[A] = dao.run(dbio)

}

/**
 * Supports creation of a new persisted record.
 *
 * @tparam Pending   the type of the request that will be used to create the record.
 * @tparam Persisted the type of the persisted item.
 */
trait CreationService[Pending, Persisted <: HasMetaData[Persisted] with Identifiable[_, Persisted]] {

  def dao: BaseDao[_, Persisted, Pending]

  def create(item: Pending, ignoreWarning: Boolean = false)(implicit ec: ExecutionContext): Future[ValidationNel[String, Persisted]] =
    dao.run(dao.create(item)).map(_.successNel)
}

/**
 * Supports finding persisted items and details of items.
 *
 * @tparam Persisted the type of the persisted item.
 * @tparam Details   the type of the data in the detailed item.
 */
trait FinderService[Ident, Persisted <: HasMetaData[Persisted] with Identifiable[Ident, Persisted], Details] {

  def dao: BaseDao[Ident, Persisted, _]

  def findDetailsById(id: Ident)(implicit ec: ExecutionContext): Future[Option[Details]]

  def findById(id: Ident)(implicit ec: ExecutionContext): Future[Option[Persisted]] = dao.run {
    dao.findOptionById(id)
  }

  def fetchAll()(implicit ec: ExecutionContext): Future[Seq[Persisted]] = dao.run {
    dao.fetchAll()
  }
}

/**
 * Supports updating items.
 *
 * @tparam Persisted the type of the persisted item.
 */
trait UpdateService[Ident, Persisted <: HasMetaData[Persisted] with Identifiable[Ident, Persisted]] {

  def dao: BaseDao[Ident, Persisted, _]

  def update(item: Persisted, ignoreWarning: Boolean = false)(implicit ec: ExecutionContext): Future[ValidationNel[String, Persisted]] =
    dao.run(dao.update(item)).map(_.successNel)

  def update(id: Ident)(u: Persisted => Persisted)(implicit ec: ExecutionContext): Future[ValidationNel[String, Persisted]] = dao.run {
    OptionT[DBIO, Persisted](dao.findOptionById(id))
      .toRight(s"[$id] does not exist.")
      .flatMap(existing => EitherT.right[DBIO, String, Persisted](dao.update(u(existing))))
      .run
      .map(_.validationNel)
  }
}

/**
 * Supports deleting items by id.
 */
trait DeletionService[Ident] {
  def dao: BaseDao[Ident, _, _]

  def delete(id: Ident)(implicit ec: ExecutionContext): Future[Ident] =
    dao.run(dao.deleteById(id)).map(_ => id)
}
