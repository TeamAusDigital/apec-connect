package org.ausdigital.apecconnect.db.model

/**
  * DB Entities that use a Long as the id.
  */
trait Identifiable[M] {
  val id: Long

  def updateId(id: Long = id): M

}
