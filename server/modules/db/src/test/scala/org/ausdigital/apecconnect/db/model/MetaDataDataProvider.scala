package org.ausdigital.apecconnect.db.model

import java.time.LocalDateTime

/**
  * Provides instances of [[MetaData]] for testing.
  */
object MetaDataDataProvider {

  val metaData = MetaData(
    recordStatus = RecordStatus.Active,
    dateCreated = LocalDateTime.now(),
    lastUpdated = LocalDateTime.now,
    version = 0L
  )

}
