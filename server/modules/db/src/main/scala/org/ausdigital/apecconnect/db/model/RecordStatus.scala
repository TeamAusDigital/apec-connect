package org.ausdigital.apecconnect.db.model

import enumeratum.values.{ IntEnumEntry, IntPlayEnum }

import scala.collection.immutable.IndexedSeq

/**
 * Status of records.
 *
 * We can soft delete records or archive old records using this enumeration.
 */
sealed abstract class RecordStatus(val value: Int, val name: String) extends IntEnumEntry

case object RecordStatus extends IntPlayEnum[RecordStatus] {

  case object Active extends RecordStatus(0, "Active")
  case object Deleted extends RecordStatus(1, "Deleted")
  case object Archived extends RecordStatus(2, "Archived")

  val values: IndexedSeq[RecordStatus] = findValues
}

