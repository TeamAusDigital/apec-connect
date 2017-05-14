package org.ausdigital.apecconnect.invoice.services

import javax.inject.Inject

import com.google.inject.Singleton
import org.ausdigital.apecconnect.db.services.SimpleRecordService
import org.ausdigital.apecconnect.invoice.dao.InvoiceDao
import org.ausdigital.apecconnect.invoice.model.Invoice.InvoiceData

import scala.concurrent.ExecutionContext

@Singleton
class InvoiceService @Inject()(override val dao: InvoiceDao)(implicit val executionContext: ExecutionContext)
  extends SimpleRecordService[InvoiceData] {

}
