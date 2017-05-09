package org.ausdigital.apecconnect.db.dao

import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile

/**
  * Trait that contains generic slick db handling code to be mixed in with DAOs
  */
trait SlickDao extends HasDatabaseConfigProvider[JdbcProfile] {

}
