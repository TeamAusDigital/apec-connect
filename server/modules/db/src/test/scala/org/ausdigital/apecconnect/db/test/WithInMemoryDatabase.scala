package org.ausdigital.apecconnect.db.test

import au.com.agiledigital.rest.tests.WithApplicationBuilder
import org.ausdigital.apecconnect.common.tests.BaseApplications
import org.specs2.execute.{AsResult, Result}
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.BindingKey
import play.api.test.Helpers
import play.db.NamedDatabaseImpl

import scala.concurrent.ExecutionContext

/**
  * Provides a running application with an in-memory database.
  */
class WithInMemoryDatabase(databaseName: String = "default") extends WithApplicationBuilder(WithInMemoryDatabase.databaseApplicationBuilder) {

  def dbConfigProvider: DatabaseConfigProvider = {
    val key = BindingKey(classOf[DatabaseConfigProvider]).qualifiedWith(new NamedDatabaseImpl(databaseName))
    app.injector.instanceOf(key)
  }

  def removeData()(implicit ec: ExecutionContext): Unit = ()

  override def around[T: AsResult](t: => T): Result = {
    Helpers.running(app) {
      removeData()(app.actorSystem.dispatcher)
      AsResult.effectively(t)
    }
  }
}

object WithInMemoryDatabase {
  /**
    * Constructs a slick aware in-memory (h2) database configuration to add to a FakeApplication.
    */
  def inMemorySlickDatabase(name: String = "default", options: Map[String, String] = Map("MODE" -> "PostgreSQL")): Map[String, String] = {
    val optionsForDbUrl = options.map { case (k, v) => k + "=" + v }.mkString(";", ";", "")

    Map(
      ("slick.dbs." + name + ".driver") -> "slick.driver.H2Driver$",
      ("slick.dbs." + name + ".db.driver") -> "org.h2.Driver",
      ("slick.dbs." + name + ".db.url") -> ("jdbc:h2:mem:play-test-" + scala.util.Random.nextInt + optionsForDbUrl),
      ("slick.dbs." + name + ".db.connectionTimeout") -> "30s"
    )
  }

  def databaseApplicationBuilder = BaseApplications.appBuilder.configure(
    inMemorySlickDatabase()
  )

}