package org.ausdigital.apecconnect.db.model

/**
 * Type that given an id and metadata can create a persisted instance of itself.
 *
 * Use this is the pending type does not match the persisted (or record data) type.
 *
 * @tparam P the type of the persisted instance.
 */
trait Persistable[P] {
  def persisted(metaData: MetaData): P
}
