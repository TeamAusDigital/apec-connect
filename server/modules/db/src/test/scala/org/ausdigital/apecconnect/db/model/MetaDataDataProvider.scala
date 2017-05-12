package org.ausdigital.apecconnect.db.model

import org.joda.time.DateTime

/**
  * Provides instances of [[MetaData]] for testing.
  */
object MetaDataDataProvider {

  val metaData = MetaData(
    recordStatus = RecordStatus.Active,
    dateCreated = DateTime.now(),
    lastUpdated = DateTime.now,
    version = 0L
  )

}
