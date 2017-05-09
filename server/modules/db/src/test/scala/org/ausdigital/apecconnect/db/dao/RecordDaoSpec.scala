package org.ausdigital.apecconnect.db.dao

import java.time.Clock

import au.com.agiledigital.rest.tests.BaseSpec
import org.ausdigital.apecconnect.common.query.Query
import org.ausdigital.apecconnect.db.model.Record
import org.ausdigital.apecconnect.db.test.WithInMemoryDatabase
import org.specs2.concurrent.ExecutionEnv
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{ JsObject, Json }
import slick.dbio._
import slick.driver.JdbcProfile

import scala.concurrent.duration._
import scala.concurrent.{ Await, ExecutionContext }
import scala.language.postfixOps

trait RecordDaoSpec[M, D <: RecordDao[M]] extends BaseSpec {

  type Context <: DaoContext[M, D]

  implicit def ev: ExecutionEnv

  def createContext(dbConfigProvider: DatabaseConfigProvider, clock: Clock): Context

  "Being a record" should {
    "Finding a instance by id" should {
      "return none if no instance with that id exists" in new WithDao {
        val result = db.run(for {
          created <- context.create(context.pending(0))
          found <- dao.findOptionById(created.id + 1000)
        } yield found)

        result must beNone.awaitFor(defaultAwait)
      }

      "return the instance if it exists" in new WithDao {
        val result = db.run(for {
          created <- context.create(context.pending(0))
          found <- dao.findOptionById(created.id)
        } yield (created, found))

        val (created, found) = Await.result(result, defaultAwait)
        found must beSome(created)
      }

      "return none if the instance has been soft-deleted" in new WithDao {
        val result = db.run(for {
          created <- context.create(context.pending(0))
          _ <- dao.delete(created)
          found <- dao.findOptionById(created.id)
        } yield found)

        result must beNone.awaitFor(defaultAwait)
      }
    }

    "Finding all instances" should {
      "return nothing if no instances have been created" in new WithDao {
        val result = db.run(for {
          found <- dao.fetchAll()
        } yield found)

        result must beEmpty[Seq[Record[M]]].awaitFor(defaultAwait)
      }
      "return the active instances" in new WithDao {
        val result = db.run(for {
          created1 <- context.create(context.pending(0))
          created2 <- context.create(context.pending(1))
          created3 <- context.create(context.pending(2))
          _ <- dao.delete(created3)
          created4 <- context.create(context.pending(3))
          found <- dao.fetchAll()
        } yield (created1, created2, created4, found))

        val (created1, created2, created4, found) = Await.result(result, defaultAwait)

        found must_=== Seq(created1, created2, created4)
      }
    }

    "Querying records" should {
      "return nothing if no instances have been created" in new WithDao {
        val result = db.run(for {
          found <- dao.query(Query(None, None, None, None, Json.obj()))
        } yield found)

        result must beLike[(Int, Seq[Record[M]])] {
          case (total: Int, results: Seq[Record[M]]) =>
            (total must_=== 0) and
              (results must_=== Seq())
        }.awaitFor(defaultAwait)
      }
      "support filtering by id" in new WithDao {
        val result = db.run(for {
          _ <- context.create(pending(0))
          _ <- context.create(pending(1))
          created <- context.create(pending(2))
          _ <- context.create(pending(3))
          found <- dao.query(Query(None, None, None, None, Json.obj("id" -> created.id)))
        } yield (created, found))

        result must beLike[(Record[M], (Int, Seq[Record[M]]))] {
          case (created: Record[M], (total: Int, results: Seq[Record[M]])) =>
            (total must_=== 1) and
              (results must_=== Seq(created))
        }.awaitFor(defaultAwait)
      }
      "support filtering by id and getting no matches" in new WithDao {
        val result = db.run(for {
          _ <- context.create(pending(0))
          _ <- context.create(pending(1))
          created <- context.create(pending(2))
          _ <- context.create(pending(3))
          found <- dao.query(Query(None, None, None, None, Json.obj("id" -> (created.id + 1000))))
        } yield found)

        result must beLike[(Int, Seq[Record[M]])] {
          case (total: Int, results: Seq[Record[M]]) =>
            (total must_=== 0) and
              (results must_=== Seq())
        }.awaitFor(defaultAwait)
      }
      "support paging" in new WithDao {
        val result = db.run(for {
          _ <- context.create(pending(0))
          _ <- context.create(pending(1))
          _ <- context.create(pending(2))
          _ <- context.create(pending(3))
          found <- dao.query(Query(Some(1), Some(2), None, None, Json.obj()))
        } yield found)

        result must beLike[(Int, Seq[Record[M]])] {
          case (total: Int, results: Seq[Record[M]]) =>
            (total must_=== 4) and
              (results.size must_=== 2)
        }.awaitFor(defaultAwait)
      }
    }

    "Updating an instance" should {
      "update the data" in new WithDao {
        val result = db.run(for {
          created <- context.create(context.pending(0))
          updated <- dao.update(context.updated(created))
        } yield (created, updated))

        val (created, updated) = Await.result(result, defaultAwait)

        val expected = context.updated(created).updateMetaData(updated.metaData)
        updated must_=== expected
      }
      "update the version id" in new WithDao {
        val result = db.run(for {
          created <- context.create(context.pending(0))
          updated1 <- dao.update(context.updated(created))
          updated2 <- dao.update(context.updated(updated1))
          updated3 <- dao.update(context.updated(updated2))
          updated4 <- dao.update(context.updated(updated3))
        } yield updated4)

        val updated = Await.result(result, defaultAwait)

        updated.metaData.version must_=== 4L
      }
      "not update other instances" in new WithDao {
        val result = db.run(for {
          created1 <- context.create(context.pending(0))
          created2 <- context.create(context.pending(2))
          created3 <- context.create(context.pending(3))
          updated <- dao.update(context.updated(created2))
          found1 <- dao.findById(created1.id)
          found2 <- dao.findById(created2.id)
          found3 <- dao.findById(created3.id)

        } yield (found1, found2, found3))

        val (found1, found2, found3) = Await.result(result, defaultAwait)

        found1.metaData.version must_=== 0L
        found2.metaData.version must_=== 1L
        found3.metaData.version must_=== 0L
      }
    }
  }

  class WithDao extends WithInMemoryDatabase with ContextRemover with DaoContext[M, D] {

    lazy val clock = Clock.systemDefaultZone()

    lazy val context: Context = createContext(dbConfigProvider, clock)

    override lazy val dao = context.dao

    override def updated(existing: Record[M]): Record[M] = context.updated(existing)

    override def create(pending: M)(implicit executionContext: ExecutionContext): DBIO[Record[M]] = context.create(pending)(executionContext)

    override def before(implicit executionContext: ExecutionContext): DBIO[Any] = context.before(executionContext)

    override def pending(index: Int): M = context.pending(index)

    override def updated(existing: M): M = context.updated(existing)
  }
}

trait ContextRemover {

  self: WithInMemoryDatabase with DaoContext[_, _] =>

  override def removeData()(implicit ec: ExecutionContext): Unit = {
    Await.result(db.run(before(ec)), 10 seconds)
  }
}

trait DaoContext[M, D <: RecordDao[M]] {
  def dao: D

  def clock: Clock

  def dbConfigProvider: DatabaseConfigProvider

  lazy val driver = dbConfigProvider.get[JdbcProfile].driver

  lazy val db = dbConfigProvider.get[JdbcProfile].db

  def pending(index: Int): M

  def updated(existing: Record[M]): Record[M] = existing.copy(data = updated(existing.data))

  def updated(existing: M): M

  def create(pending: M)(implicit executionContext: ExecutionContext): DBIO[Record[M]] = dao.create(pending)

  def before(implicit executionContext: ExecutionContext): DBIO[Any] = {
    import driver.api._
    dao.tableQuery.delete
  }
}
