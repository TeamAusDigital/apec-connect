package org.ausdigital.apecconnect.db.model

/**
  * Provides instances of [[Record]] for test.
  */
object RecordDataProvider {

  def record[D](data: D, id: Long = 1L): Record[D] = Record(id, data, MetaDataDataProvider.metaData)

}
