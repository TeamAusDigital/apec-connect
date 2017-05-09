package org.ausdigital.apecconnect.db.dao

import java.time.LocalDateTime

import au.com.agiledigital.dao.slick.Lens._
import au.com.agiledigital.dao.slick._
import org.ausdigital.apecconnect.db.model.HasMetaData
import org.ausdigital.apecconnect.common.query.{ Query => ApiQuery }
import org.ausdigital.apecconnect.db.model.{ HasMetaData, Identifiable, MetaData, Record, RecordStatus => ModelRecordStatus }
import slick.ast.{ BaseTypedType, ScalaBaseType }
import slick.backend.DatabasePublisher

import scala.concurrent.{ ExecutionContext, Future }

trait RecordDao[P] extends BaseDao[Record[P], P] {
  self: BaseDbTableDefinitions =>

  import driver.api._

  override def entity(pending: P): Record[P] = {
    val now = LocalDateTime.now(clock)
    Record(-1, pending, MetaData(
      active,
      now,
      now,
      0L
    ))
  }

  protected def filtersFromQuery(filterQuery: ApiQuery): Set[(EntityTable) => Rep[Boolean]] =
    (filterQuery.filters \ "id").asOpt[Long].map(id => (i: EntityTable) => i.id === id).toSet

  protected def filterByQuery(filterQuery: ApiQuery, query: Query[EntityTable, Record[P], Seq]): Query[EntityTable, Record[P], Seq] = {
    val filters = filtersFromQuery(filterQuery)

    filters.foldLeft(query) {
      case (acc, e) => acc.filter(e)
    }
  }

  def query(query: ApiQuery, fetchSize: Int = CrudActions.defaultFetchSize)(implicit exc: ExecutionContext): DBIO[(Int, Seq[Record[P]])] = {

    val filteredQuery = filterByQuery(query, baseQuery)

    val limitedQuery = (query.page, query.perPage) match {
      case (Some(page), Some(perPage)) => filteredQuery.drop((page - 1) * perPage).take(perPage)
      case _ => filteredQuery
    }

    val resultsQuery = limitedQuery
      .result
      .transactionally
      .withStatementParameters(fetchSize = fetchSize)

    filteredQuery.size.result.zip(resultsQuery)
  }

}

trait BaseDao[M <: HasMetaData[M] with Identifiable[M], P] extends SlickDao with EntityActions[M, P] with MetaDataDao[M, P] with IdentifiableDao[M, P] {

  self: BaseDbTableDefinitions =>

  import driver.api._

  override type EntityTable <: TableWithId[M] with TableWithMetaData[M]

  /**
    * Runs the DBIO action transactionally.
    * @param action the action to be run.
    * @tparam T the type of the result that will be produced when the action is run.
    * @return a future that will contain the result of running the action.
    */
  def run[T](action: DBIO[T]): Future[T] = dbConfig.db.run(action.transactionally)

  /**
    * Runs the DBIO action transactionally and returns the result directly as a stream without buffering everything first.
    * @param action the action to be run.
    * @tparam T the type of the result that will be produced when the action is run.
    * @return a `Publisher` for Reactive Streams which, when subscribed to, will run the specified action and return the result.
    */
  def stream[T](action: StreamingDBIO[_, T]): DatabasePublisher[T] = dbConfig.db.stream(action.transactionally)

}

trait IdentifiableDao[I <: Identifiable[I], P] extends EntityActions[I, P] {
  self: BaseDbTableDefinitions =>

  import driver.api._

  override type Id = Long

  override type EntityTable <: TableWithId[I]

  override def $id(table: EntityTable): Rep[Long] = table.id

  override def baseTypedType: BaseTypedType[Long] = ScalaBaseType.longType

  override def idLens: Lens[I, Long] = lens { activity: I => activity.id } { (entry, id) => entry.updateId(id = id) }
}

trait MetaDataDao[M <: HasMetaData[M], P]
    extends SoftDeleteActions[M, P] with MetadataEntityActions[M, P] with OptimisticLocking[M] {

  self: BaseDbTableDefinitions =>

  import driver.api._

  override type EntityTable <: TableWithMetaData[M]

  override def $recordStatus(table: EntityTable): Rep[ModelRecordStatus.RecordStatus] = table.recordStatus

  override def $version(table: EntityTable): Rep[Long] = table.version

  override type RecordStatus = ModelRecordStatus.Value

  override def baseRecordStatusTypedType: BaseTypedType[ModelRecordStatus.Value] = recordStatusMapper

  override def deleted: RecordStatus = ModelRecordStatus.Deleted

  override def active: RecordStatus = ModelRecordStatus.Active

  override def versionLens: Lens[M, Long] = lens {
    activity: M => activity.metaData.version
  } {
    (entry, version) => entry.updateMetaData(metaData = entry.metaData.copy(version = version))
  }

  override def lastUpdatedLens: Lens[M, LocalDateTime] = lens {
    activity: M => activity.metaData.lastUpdated
  } {
    (entry, lastUpdated) => entry.updateMetaData(metaData = entry.metaData.copy(lastUpdated = lastUpdated))
  }

  override def dateCreatedLens: Lens[M, LocalDateTime] = lens {
    activity: M => activity.metaData.dateCreated
  } {
    (entry, dateCreated) => entry.updateMetaData(metaData = entry.metaData.copy(dateCreated = dateCreated))
  }

}
