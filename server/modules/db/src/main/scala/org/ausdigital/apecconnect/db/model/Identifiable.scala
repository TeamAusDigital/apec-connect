package org.ausdigital.apecconnect.db.model

/**
 * DB Entities that use a Long as the id.
 */
trait Identifiable[Id, M] {
  val id: Id

  def updateId(id: Id = id): M

}
