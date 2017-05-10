package org.ausdigital.apecconnect.db.dao

import java.time.Clock

import au.com.agiledigital.dao.slick.DBIOExtensions._
import au.com.agiledigital.dao.slick.Lens._
import au.com.agiledigital.dao.slick._
import au.com.agiledigital.dao.slick.exceptions.{ NoRowsAffectedException, RowNotFoundException }
import org.ausdigital.apecconnect.db.model.{ HasMetaData, Identifiable, MetaData, Persistable, Record, RecordId, RecordStatus => ModelRecordStatus }
import org.joda.time.DateTime
import slick.ast.BaseTypedType
import slick.basic.DatabasePublisher

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success }

trait RecordDao[Data] extends BaseDao[RecordId[Record[Data]], Record[Data], Data] {

  self: BaseDbTableDefinitions =>

  import profile.api._

  override def baseTypedType: BaseColumnType[RecordId[Record[Data]]] = MappedColumnType.base[RecordId[Record[Data]], Long](
    recordId => recordId.value,
    id => new RecordId[Record[Data]](id)
  )

  override def entity(pending: Data): Record[Data] = {
    val at = now

    val metaData = MetaData(
      active,
      at,
      at,
      0L
    )

    // -1L will be filtered by Slick on INSERT and will be supplied by the database.
    Record(RecordId(-1L), pending, metaData)
  }

}

/**
 * Specialises data access to pending types that implement [[Persistable]].
 *
 * @tparam Persisted the type of the persisted model.
 * @tparam Pending the pending type that implements Persistable.
 */
trait PersistableCreate[Ident, Persisted, Pending <: Persistable[Persisted]] extends DefaultEntityCreationActions[Persisted, Pending] with EntityActions[Persisted, Pending] {

  override def entity(pending: Pending): Persisted = {
    val at = DateTime.now

    val metaData = MetaData(
      ModelRecordStatus.Active,
      at,
      at,
      0L
    )

    // id will be filtered by Slick on INSERT and will be supplied by the database.
    pending.persisted(metaData)
  }
}

/**
 * DAO that supports metadata, soft-deletion, optimistic locking and identification by a Long.
 *
 * @tparam Persisted the type of the model persisted by this DAO.
 * @tparam Pending the pending type.
 */
trait BaseDao[Ident, Persisted <: HasMetaData[Persisted] with Identifiable[Ident, Persisted], Pending]
    extends SlickDao
    with EntityActions[Persisted, Pending]
    with MetaDataDao[Persisted, Pending]
    with IdentifiableDao[Ident, Persisted, Pending] { self: BaseDbTableDefinitions =>

  import profile.api._

  override type EntityTable <: TableWithId[Ident, Persisted] with TableWithMetaData[Persisted]

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

  override def fetchAll(fetchSize: Int = QueryActions.defaultFetchSize)(implicit exc: ExecutionContext): StreamingDBIO[Seq[Persisted], Persisted] =
    baseQuery.result.transactionally
      .withStatementParameters(fetchSize = fetchSize)
}

/**
 * Partial implementation of EntityActions for the DataPOS the specific identifier type.
 *
 * @tparam I the type that will be identified by the id.
 * @tparam Pending the pending type.
 */
trait IdentifiableDao[Ident, I <: Identifiable[Ident, I], Pending] extends EntityActions[I, Pending] { self: BaseDbTableDefinitions =>

  import profile.api._

  override type Id = Ident

  override type EntityTable <: TableWithId[Ident, I]

  override def $id(table: EntityTable): Rep[Ident] = table.id
  override def idLens: Lens[I, Ident] =
    lens { activity: I =>
      activity.id
    } { (entry, id) =>
      entry.updateId(id = id)
    }

}

/**
 * Provides soft-deletion, metadata (creation date, last updated) and optimistic locking for types that
 * implement [[HasMetaData]].
 */
trait MetaDataDao[M <: HasMetaData[M], Pending] extends SoftDeleteActions[M] with MetadataEntityActions[M, Pending, DateTime] { self: BaseDbTableDefinitions =>

  import profile.api._

  def clock: Clock

  override type EntityTable <: TableWithMetaData[M]

  override def $recordStatus(table: EntityTable): Rep[RecordStatus] = table.recordStatus

  def $version(table: EntityTable): Rep[Long] = table.version

  override type RecordStatus = ModelRecordStatus

  override def baseRecordStatusTypedType: BaseTypedType[RecordStatus] = recordStatusMapper

  override def deleted: RecordStatus = ModelRecordStatus.Deleted

  override def active: RecordStatus = ModelRecordStatus.Active

  override def now: DateTime = new DateTime(clock.millis)

  // Overriden to suppress the optimistic locking check but to retain the version increment.
  override protected def update(id: Id, versionable: M)(implicit exc: ExecutionContext): DBIO[M] = {

    // extract current version
    val currentVersion = versionLens.get(versionable)

    // model with incremented version
    val modelWithNewVersion = versionLens.set(versionable, currentVersion + 1)

    val triedUpdate = filterById(id).update(modelWithNewVersion).mustAffectOneSingleRow.asTry

    triedUpdate.flatMap {
      case Success(_) => DBIO.successful(modelWithNewVersion)
      case Failure(NoRowsAffectedException) => DBIO.failed(new RowNotFoundException(modelWithNewVersion))
      case Failure(ex) => DBIO.failed(ex)
    }
  }

  /**
   * Archives the instance identified by the provided id. Does *not* fail if no matching instances exist.
   *
   * @return the number of instances archived (expected to be 0 or 1).
   */
  def archiveById(id: Id)(implicit exc: ExecutionContext): DBIO[Int] = filterById(id).map(_.recordStatus).update(ModelRecordStatus.Archived)

  def versionLens: Lens[M, Long] =
    lens { entity: M =>
      entity.metaData.version
    } { (entry, version) =>
      entry.updateMetaData(metaData = entry.metaData.copy(version = version))
    }

  override def lastUpdatedLens: Lens[M, DateTime] =
    lens { entity: M =>
      entity.metaData.lastUpdated
    } { (entry, lastUpdated) =>
      entry.updateMetaData(metaData = entry.metaData.copy(lastUpdated = lastUpdated))
    }

  override def dateCreatedLens: Lens[M, DateTime] =
    lens { entity: M =>
      entity.metaData.dateCreated
    } { (entry, dateCreated) =>
      entry.updateMetaData(metaData = entry.metaData.copy(dateCreated = dateCreated))
    }

}
