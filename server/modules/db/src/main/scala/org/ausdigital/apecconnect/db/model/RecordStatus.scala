package org.ausdigital.apecconnect.db.model

/**
  * Status of records.
  *
  * We can soft delete records or archive old records using this enumeration.
  */
object RecordStatus extends Enumeration() {
  type RecordStatus = Value
  val Active = Value(0, "Active")
  val Deleted = Value(1, "Deleted")
  val Archived = Value(2, "Archived")
}
