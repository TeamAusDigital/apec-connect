package org.ausdigital.apecconnect.db.dao

import org.ausdigital.apecconnect.common.model.ApecConnectEnumEntry
import slick.jdbc.{ JdbcProfile, JdbcType }

import scala.reflect.ClassTag

trait EnumerationMapping {

  /**
   * Creates a slick mapper / type for the provided enumeration.
   *
   * @param e       the enumeration to be mapped.
   * @param profile the driver that will support the mapping.
   * @tparam A the type of the enumeration to be mapped.
   * @return the mapper / type.
   */
  def mapper[A <: Enumeration](e: A)(implicit profile: JdbcProfile): JdbcType[A#Value] = {
    import profile.api._
    MappedColumnType.base[A#Value, Int](
      a => a.id,
      id => e.apply(id)
    )
  }

  /**
   * Creates a slick mapper / type for the provided enumeratum enumeration.
   *
   * @param e       the enumeration to be mapped.
   * @param profile the driver that will support the mapping.
   * @tparam A the type of the enumeration to be mapped.
   * @return the mapper / type.
   */
  def enumeratumMapper[A <: ApecConnectEnumEntry: ClassTag](values: IndexedSeq[A])(implicit profile: JdbcProfile): JdbcType[A] = {
    import profile.api._

    val entries = values.map(v => v.value -> v).toMap

    val existingValuesString = entries.keys.mkString(", ")

    MappedColumnType.base[A, Int](
      entry => entry.value,
      value => entries.getOrElse(value, throw new NoSuchElementException(s"[$value] is not a member of ApecConnectEnumEntry [$existingValuesString]."))
    )
  }
}