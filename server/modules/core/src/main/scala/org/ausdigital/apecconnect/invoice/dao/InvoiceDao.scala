package org.ausdigital.apecconnect.invoice.dao

import java.time.Clock
import javax.inject.Inject

import org.ausdigital.apecconnect.db.dao.RecordDao
import org.ausdigital.apecconnect.invoice.model.Invoice.InvoiceData
import play.api.db.slick.DatabaseConfigProvider


class InvoiceDao @Inject() (override val dbConfigProvider: DatabaseConfigProvider, override val clock: Clock)
  extends RecordDao[InvoiceData] with InvoiceDbTableDefinitions {

  import profile.api._

  override type EntityTable = Invoices

  override def tableQuery: TableQuery[Invoices] = TableQuery[Invoices]


}
