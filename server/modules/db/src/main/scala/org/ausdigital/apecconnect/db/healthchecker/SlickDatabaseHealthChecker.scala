package org.ausdigital.apecconnect.db.healthchecker

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import au.com.agiledigital.healthchecker._
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.{ BindingKey, Injector }
import play.api.{ Application, Configuration }
import play.db.NamedDatabaseImpl
import slick.backend.DatabaseConfig
import slick.dbio
import slick.driver.JdbcProfile

import scala.concurrent.duration.{ FiniteDuration, _ }
import scala.concurrent.{ ExecutionContext, Future }
import scala.language.postfixOps
import scalaz.\/

/**
  * Creates instances of the [[SlickDatabaseHealthChecker]].
  */
class SlickDatabaseHealthCheckerFactory @Inject() (application: Application, injector: Injector) extends HealthCheckerFactory {
  override def createHealthChecker(checkerConfiguration: Configuration): HealthChecker =
    new SlickDatabaseHealthChecker(application, injector, checkerConfiguration)
}

/**
  * Health checker that executes the configured SQL against the configured database.
  *
  * @param application the application to be checked.
  * @param injector the injector that will be used to acquired the database config provider.
  * @param configuration the configuration for this checker.
  */
class SlickDatabaseHealthChecker(application: Application, injector: Injector, override val configuration: Configuration)
    extends BaseHealthChecker with ConfigurationBasedHealthChecker {

  /** The name of the slick db configuration that will be used to perform the check. */
  val configName = configuration.getString("db").getOrElse("default")

  /** The SQL statement that will be executed to perform the check. */
  val checkSql = configuration.getString("checkSql").getOrElse("SELECT 1;")

  val timeout = configuration.getMilliseconds("timeout").
    map(FiniteDuration(_, TimeUnit.MILLISECONDS)).
    getOrElse(5 seconds)

  val name = s"Slick DB [$configName]"

  override def doCheck()(implicit ec: ExecutionContext): Future[HealthCheckOutcome] = {
    getDatabaseConfig.map { databaseConfig =>
      executeCheckStatement(databaseConfig)
    }.
      leftMap(Future.successful).
      merge
  }

  private def getDatabaseConfig(implicit ec: ExecutionContext): \/[HealthCheckOutcome, DatabaseConfig[JdbcProfile]] = {
    \/ fromTryCatchNonFatal {
      val key = BindingKey(classOf[DatabaseConfigProvider]).qualifiedWith(new NamedDatabaseImpl(configName))
      val databaseConfigProvider = injector.instanceOf(key)
      databaseConfigProvider.get[JdbcProfile]
    } leftMap { t =>
      HealthCheckOutcome(
        status = HealthCheckStatus.Error,
        value = None,
        message = Some(s"Failed to get DatabaseConfig for [$configName]."),
        throwable = Some(t)
      )
    }
  }

  private def executeCheckStatement(databaseConfig: DatabaseConfig[JdbcProfile])(implicit ec: ExecutionContext): Future[HealthCheckOutcome] = {
    import databaseConfig.driver.api._
    val query: dbio.DBIOAction[Vector[Int], NoStream, Effect] = sql"#$checkSql".as[Int]

    val config = databaseConfig.config.withoutPath("db.password")

    val queryFuture: Future[HealthCheckOutcome] = databaseConfig.db.run(query) map { result =>
      HealthCheckOutcome(
        status = HealthCheckStatus.Ok,
        value = Some(result),
        message = Some(s"Executed [$checkSql] against [$configName] ([$config]) and got result [$result]."),
        throwable = None
      )
    }

    val timeoutFuture = akka.pattern.after(timeout, application.actorSystem.scheduler)(Future.successful(
      HealthCheckOutcome(
        status = HealthCheckStatus.Error,
        value = None,
        message = Some(s"Timed out after [$timeout] waiting for [$checkSql] against [$configName] ([$config])."),
        throwable = None
      )
    ))

    Future.firstCompletedOf(Seq(timeoutFuture, queryFuture))
  }

}
